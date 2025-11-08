package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.exchange_voucher.request.CreateExchangeVoucherRequest;
import br.com.marcosprado.timesbackend.dto.exchange_voucher.response.ExchangeRequestResponse;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.ExchangeRequestRepository;
import br.com.marcosprado.timesbackend.repository.OrderItemRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class ExchangeRequestService {

    private final ClientRepository clientRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ExchangeRequestVoucherService exchangeRequestVoucherService;

    public ExchangeRequestService(
            ClientRepository clientRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            OrderItemRepository orderItemRepository,
            ExchangeRequestRepository exchangeRequestRepository,
            ExchangeRequestVoucherService exchangeRequestVoucherService
    ) {
        this.clientRepository = clientRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.exchangeRequestVoucherService = exchangeRequestVoucherService;
    }

    @Transactional
    public ExchangeRequestResponse requestExchange(CreateExchangeVoucherRequest createExchangeVoucherRequest, Integer clientId) {
        var purchaseOrderId = createExchangeVoucherRequest.purchaseOrder();

        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(ResourceNotFoundException::purchaseOrderNotFound);

        if (!purchaseOrder.isDelivered()) {
            throw OperationNotAllowedException.cannotExchangeOrderNotDelivered();
        }

        ClientAggregate client = this.clientRepository.findById(clientId)
                .orElseThrow(() -> ResourceNotFoundException.clientNotFound(clientId));

        if (!purchaseOrder.getClient().getId().equals(client.getId())) {
            throw OperationNotAllowedException.requestDoesntBelongClient(purchaseOrder.getPurchaseOrderNumber());
        }

        Set<OrderItem> itemsToExchange = new HashSet<>();
        for (Long itemId : createExchangeVoucherRequest.orderItensId()) {
            OrderItem item = orderItemRepository.findById(itemId)
                    .orElseThrow(() -> ResourceNotFoundException.orderItemNotFound(itemId));

            if (!item.getPurchaseOrder().getId().equals(purchaseOrder.getId())) {
                throw new OperationNotAllowedException(
                        "Item " + itemId + " não pertence ao pedido informado"
                );
            }

            itemsToExchange.add(item);
        }

        ExchangeRequest exchangeRequest = new ExchangeRequest(
                purchaseOrder,
                client,
                itemsToExchange,
                createExchangeVoucherRequest.exchangeType()
        );

        purchaseOrder.markAsExchangeProcess();
        purchaseOrderRepository.save(purchaseOrder);

        ExchangeRequest saved = exchangeRequestRepository.save(exchangeRequest);
        return ExchangeRequestResponse.fromEntity(saved);
    }

    @Transactional
    public Page<ExchangeRequestResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("requestedAt").descending());

        Page<ExchangeRequest> exchangeRequests = this.exchangeRequestRepository.findAll(pageable);
        return exchangeRequests.map(ExchangeRequestResponse::fromEntity);
    }

    @Transactional
    public ExchangeRequestResponse approveExchange(Long exchangeId) {
        ExchangeRequest exchangeRequest = this.exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(ResourceNotFoundException::exchangeRequestNotFound);

        exchangeRequest.approve();
        return ExchangeRequestResponse.fromEntity(this.exchangeRequestRepository.save(exchangeRequest));
    }

    @Transactional
    public ExchangeRequestResponse rejectExchange(Long exchangeId) {
        ExchangeRequest exchangeRequest = this.exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(ResourceNotFoundException::exchangeRequestNotFound);

        exchangeRequest.reject();
        return ExchangeRequestResponse.fromEntity(this.exchangeRequestRepository.save(exchangeRequest));
    }

    @Transactional
    public ExchangeRequestResponse markAsInTransit(Long exchangeId) {
        ExchangeRequest exchangeRequest = this.exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(ResourceNotFoundException::exchangeRequestNotFound);

        exchangeRequest.markAsInTransit();
        return ExchangeRequestResponse.fromEntity(this.exchangeRequestRepository.save(exchangeRequest));
    }

    @Transactional
    public ExchangeRequestResponse confirmItemsReceived(Long exchangeId) {
        ExchangeRequest exchangeRequest = this.exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(ResourceNotFoundException::exchangeRequestNotFound);

        exchangeRequest.confirmItemsReceived();
        return ExchangeRequestResponse.fromEntity(this.exchangeRequestRepository.save(exchangeRequest));
    }

    @Transactional
    public ExchangeRequestResponse complete(Long exchangeId) {
        ExchangeRequest exchangeRequest = this.exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(ResourceNotFoundException::exchangeRequestNotFound);

        var exchangeRequestVoucher = this.exchangeRequestVoucherService.generateByExchangeRequest(exchangeRequest);

        exchangeRequest.complete(exchangeRequestVoucher);
        return ExchangeRequestResponse.fromEntity(this.exchangeRequestRepository.save(exchangeRequest));
    }

}
