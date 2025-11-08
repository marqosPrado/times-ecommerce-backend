package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.*;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.CreatePurchaseOrderRequest;
import br.com.marcosprado.timesbackend.dto.PurchaseOrderResponse;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.ExchangeVoucherRequestRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ClientRepository clientRepository;
    private final ExchangeVoucherRequestRepository exchangeVoucherRequestRepository;
    private final ProductService productService;
    private final AddressService addressService;
    private final VoucherService voucherService;
    private final CreditCardService creditCardService;
    private final PaymentService paymentService;

    public PurchaseOrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            ClientRepository clientRepository,
            ExchangeVoucherRequestRepository exchangeVoucherRequestRepository,
            ProductService productService,
            AddressService addressService,
            VoucherService voucherService,
            CreditCardService creditCardService,
            PaymentService paymentService
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.clientRepository = clientRepository;
        this.exchangeVoucherRequestRepository = exchangeVoucherRequestRepository;
        this.productService = productService;
        this.addressService = addressService;
        this.voucherService = voucherService;
        this.creditCardService = creditCardService;
        this.paymentService = paymentService;
    }

    @Transactional
    public PurchaseOrderResponse create(CreatePurchaseOrderRequest request, Integer clientId) {
        ClientAggregate client = clientRepository.findById(clientId)
                .orElseThrow(() -> ResourceNotFoundException.clientNotFound(clientId));

        List<OrderItem> orderItem = getOrderItems(request);

        AddressAggregate address = addressService.findAddressById(request.addressId())
                .orElseThrow(() -> ResourceNotFoundException.addressNotFoud(request.addressId()));

        Voucher voucher = getVoucher(request);

        if (request.exchangeVouchersId().isEmpty() && request.creditCardId().isEmpty()) {
            throw OperationNotAllowedException.invalidPaymentMethod();
        }

        Set<ExchangeRequestVoucher> voucherRequests = request.exchangeVouchersId() != null
                ? request.exchangeVouchersId().stream()
                .map(id -> exchangeVoucherRequestRepository.findById(id)
                        .orElseThrow(() -> ResourceNotFoundException.exchangeRequestVoucherNotFound(id)))
                .collect(Collectors.toSet())
                : Set.of();

        Set<CreditCardAggregate> creditCards = request.creditCardId() != null
                ? request.creditCardId().stream()
                .map(id -> creditCardService.findCreditCardById(id)
                        .orElseThrow(() -> ResourceNotFoundException.creditCardNotFound(id)))
                .collect(Collectors.toSet())
                : Set.of();

        PurchaseOrder purchaseOrder = createPurchaseOrder(orderItem, address, voucher, voucherRequests, creditCards, client);

        paymentService.process(purchaseOrder);

        return PurchaseOrderResponse.fromEntity(purchaseOrderRepository.save(purchaseOrder));
    }

    private PurchaseOrder createPurchaseOrder(
            List<OrderItem> orderItem,
            AddressAggregate address,
            Voucher voucher,
            Set<ExchangeRequestVoucher> voucherRequests,
            Set<CreditCardAggregate> creditCard,
            ClientAggregate client
    ) {
        String purchaseOrderNumber = generatePurchaseOrderNumber();

        PurchaseOrder purchaseOrder = new PurchaseOrder(
                purchaseOrderNumber,
                new BigDecimal("10")
        );
        purchaseOrder.preparePurchaseOrder(
                orderItem,
                address,
                voucher,
                voucherRequests,
                creditCard,
                client
        );
        return purchaseOrder;
    }

    private Voucher getVoucher(CreatePurchaseOrderRequest request) {
        if (request.voucher().isBlank()) {
            return null;
        }

        Voucher voucher = voucherService.findVoucherByIdentifier(request.voucher())
                .orElseThrow(() -> ResourceNotFoundException.voucherNotFound(request.voucher()));

        if (!voucher.isValid() || voucher.getCupomType().isExchange()) {
            throw OperationNotAllowedException.cannotUseInvalidVoucher();
        }

        return voucher;
    }

    private List<OrderItem> getOrderItems(CreatePurchaseOrderRequest request) {
        return request.orderItem().stream()
                .map(itemRequest -> {
                    Product product = productService.findById(itemRequest.productId())
                            .orElseThrow(() -> ResourceNotFoundException.productNotFound(itemRequest.productId()));

                    return new OrderItem(product, itemRequest.quantity());
                })
                .toList();
    }

    private String generatePurchaseOrderNumber() {
        return String.format("PED-%d",  System.currentTimeMillis());
    }

    public Page<PurchaseOrderResponse> findAllByClient(Integer clientId, int page, int size) {
        ClientAggregate client = this.clientRepository.findById(clientId)
                .orElseThrow(() -> ResourceNotFoundException.clientNotFound(clientId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PurchaseOrder> orders = this.purchaseOrderRepository.findAllByClient(client, pageable);

        return orders.map(PurchaseOrderResponse::fromEntity);
    }

    @Transactional
    public PurchaseOrderResponse aprovePurchaseOrder(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id da ordem de compra não deve ser nulo");
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::purchaseOrderNotFound);

        purchaseOrder.approvePurchaseOrder();
        return PurchaseOrderResponse.fromEntity(purchaseOrderRepository.save(purchaseOrder));
    }

    @Transactional
    public PurchaseOrderResponse markPurchaseOrderAsInTransit(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id da ordem de compra não deve ser nulo");
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::purchaseOrderNotFound);

        purchaseOrder.markAsInTransit();
        return PurchaseOrderResponse.fromEntity(purchaseOrderRepository.save(purchaseOrder));
    }

    @Transactional
    public PurchaseOrderResponse markPurchaseOrderAsDelivered(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id da ordem de compra não deve ser nulo");
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::purchaseOrderNotFound);

        purchaseOrder.markAsDelivered();
        return PurchaseOrderResponse.fromEntity(purchaseOrderRepository.save(purchaseOrder));
    }

    public Page<PurchaseOrderResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PurchaseOrder> orders = this.purchaseOrderRepository.findAll(pageable);
        return orders.map(PurchaseOrderResponse::fromEntity);
    }
}
