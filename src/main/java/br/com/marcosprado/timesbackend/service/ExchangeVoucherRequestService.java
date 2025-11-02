package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.exchange_voucher.request.CreateExchangeVoucherRequest;
import br.com.marcosprado.timesbackend.dto.exchange_voucher.response.ExchangeRequestResponse;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.ExchangeVoucherRequestRepository;
import br.com.marcosprado.timesbackend.repository.OrderItemRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ExchangeVoucherRequestService {

    private final ClientRepository clientRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ExchangeVoucherRequestRepository exchangeVoucherRequestRepository;

    public ExchangeVoucherRequestService(
            ClientRepository clientRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            OrderItemRepository orderItemRepository,
            ExchangeVoucherRequestRepository exchangeVoucherRequestRepository
    ) {
        this.clientRepository = clientRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.exchangeVoucherRequestRepository = exchangeVoucherRequestRepository;
    }

    @Transactional
    public ExchangeRequestResponse requestExchange(CreateExchangeVoucherRequest createExchangeVoucherRequest, Integer clientId) {
        var purchaseOrderId = createExchangeVoucherRequest.purchaseOrder();

        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(ResourceNotFoundException::purchaseOrderNotFound);

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

        purchaseOrder.setOrderStatus(OrderStatus.EXCHANGE_REQUESTED);
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        purchaseOrderRepository.save(purchaseOrder);

        ExchangeRequest saved = exchangeVoucherRequestRepository.save(exchangeRequest);
        return ExchangeRequestResponse.fromEntity(saved);
    }
}
