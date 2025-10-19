package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.*;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.CreatePurchaseOrderRequest;
import br.com.marcosprado.timesbackend.dto.PurchaseOrderResponse;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ClientRepository clientRepository;
    private final ProductService productService;
    private final AddressService addressService;
    private final VoucherService voucherService;
    private final CreditCardService creditCardService;

    public PurchaseOrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            ClientRepository clientRepository,
            ProductService productService,
            AddressService addressService,
            VoucherService voucherService,
            CreditCardService creditCardService
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.clientRepository = clientRepository;
        this.productService = productService;
        this.addressService = addressService;
        this.voucherService = voucherService;
        this.creditCardService = creditCardService;
    }

    @Transactional
    public PurchaseOrderResponse create(CreatePurchaseOrderRequest request, Integer clientId) {
        ClientAggregate client = clientRepository.findById(clientId)
                .orElseThrow(() -> ResourceNotFoundException.clientNotFound(clientId));

        List<OrderItem> orderItem = getOrderItems(request);

        AddressAggregate address = addressService.findAddressById(request.addressId())
                .orElseThrow(() -> ResourceNotFoundException.addressNotFoud(request.addressId()));

        Voucher voucher = getVoucher(request);

        CreditCardAggregate creditCard = creditCardService.findCreditCardById(request.creditCardId())
                .orElseThrow(() -> ResourceNotFoundException.creditCardNotFound(request.creditCardId()));

        PurchaseOrder purchaseOrder = createPurchaseOrder(orderItem, address, voucher, creditCard, client);

        return PurchaseOrderResponse.fromEntity(purchaseOrderRepository.save(purchaseOrder));
    }

    private PurchaseOrder createPurchaseOrder(
            List<OrderItem> orderItem,
            AddressAggregate address,
            Voucher voucher,
            CreditCardAggregate creditCard,
            ClientAggregate client
    ) {
        String purchaseOrderNumber = generatePurchaseOrderNumber();

        PurchaseOrder purchaseOrder = new PurchaseOrder(
                purchaseOrderNumber,
                new BigDecimal("10")
        );
        orderItem.forEach(purchaseOrder::addItem);
        purchaseOrder.setAddress(address);
        purchaseOrder.applyVoucher(voucher);
        purchaseOrder.setCreditCard(creditCard);
        purchaseOrder.setClient(client);
        return purchaseOrder;
    }

    private Voucher getVoucher(CreatePurchaseOrderRequest request) {
        Voucher voucher = null;
        if (!request.voucher().isBlank()) {
            Voucher findedVoucher = voucherService.findVoucherByIdentifier(request.voucher())
                    .orElseThrow(() -> ResourceNotFoundException.voucherNotFound(request.voucher()));

            if (!findedVoucher.isValid()) {
                throw OperationNotAllowedException.cannotUseInvalidVoucher();
            }

            if (findedVoucher.getCupomType().isExchange()) {
                throw OperationNotAllowedException.cannotUseInvalidVoucher();
            }
            voucher = findedVoucher;
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
}
