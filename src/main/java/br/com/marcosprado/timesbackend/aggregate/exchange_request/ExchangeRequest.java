package br.com.marcosprado.timesbackend.aggregate.exchange_request;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "EXCHANCE_REQUESTS")
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String exchangeNumber;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientAggregate client;

    @ManyToMany
    @JoinTable(
            name = "exchange_request_order_item",
            joinColumns = @JoinColumn(name = "exchange_request"),
            inverseJoinColumns = @JoinColumn(name = "purchase_order")
    )
    private Set<OrderItem> itensToExchange;

    @Enumerated(EnumType.STRING)
    private ExchangeType exchangeType;

    @Enumerated(EnumType.STRING)
    private ExchangeStatus exchangeStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal exchangeValue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exchange_request_voucher_id")
    private ExchangeRequestVoucher exchangeRequestVoucher;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Column
    private LocalDateTime processedAt;

    @Column
    private LocalDateTime itemsReceivedAt;

    @Column
    private LocalDateTime completedAt;

    protected ExchangeRequest() {
    }

    public ExchangeRequest(
            PurchaseOrder purchaseOrder,
            ClientAggregate client,
            Set<OrderItem> itensToExchange,
            ExchangeType exchangeType
    ) {
        if (purchaseOrder == null) {
            throw new IllegalArgumentException("Ordem de compra não deve ser nula");
        }

        if (client == null) {
            throw new IllegalArgumentException("Cliente não deve ser nulo");
        }

        if (itensToExchange == null || itensToExchange.isEmpty()) {
            throw new IllegalArgumentException("Itens para troca não devem ser vazios");
        }

        this.exchangeNumber = generateExchangeNumber();
        this.purchaseOrder = purchaseOrder;
        this.client = client;
        this.itensToExchange = itensToExchange;
        this.exchangeType = exchangeType;
        this.exchangeStatus = ExchangeStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
        this.exchangeValue = calculateExchangeValue();
    }

    private String generateExchangeNumber() {
        return String.format("TRC-%d", System.currentTimeMillis());
    }

    private BigDecimal calculateExchangeValue() {
        return this.itensToExchange.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void approve(String adminNotes) {
        if (this.exchangeStatus != ExchangeStatus.PENDING) {
            throw new IllegalStateException("Apenas solicitações pendentes podem ser aprovadas");
        }
        this.exchangeStatus = ExchangeStatus.APPROVED;
        this.processedAt = LocalDateTime.now();
    }

    public void reject(String adminNotes) {
        if (this.exchangeStatus != ExchangeStatus.PENDING) {
            throw new IllegalStateException("Apenas solicitações pendentes podem ser rejeitadas");
        }
        this.exchangeStatus = ExchangeStatus.REJECTED;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsInTransit() {
        if (this.exchangeStatus != ExchangeStatus.APPROVED) {
            throw new IllegalStateException("Apenas solicitações aprovadas podem ser marcadas como em trânsito");
        }
        this.exchangeStatus = ExchangeStatus.IN_TRANSIT;
    }

    public void confirmItemsReceived() {
        if (this.exchangeStatus != ExchangeStatus.IN_TRANSIT) {
            throw new IllegalStateException("Apenas solicitações em trânsito podem ter itens recebidos");
        }
        this.exchangeStatus = ExchangeStatus.ITEMS_RECEIVED;
        this.itemsReceivedAt = LocalDateTime.now();
    }

    public void complete(ExchangeRequestVoucher voucher) {
        if (this.exchangeStatus != ExchangeStatus.ITEMS_RECEIVED) {
            throw new IllegalStateException("Apenas solicitações com itens recebidos podem ser completadas");
        }
        this.exchangeStatus = ExchangeStatus.COMPLETED;
        this.exchangeRequestVoucher = voucher;
        this.completedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public String getExchangeNumber() {
        return exchangeNumber;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public ClientAggregate getClient() {
        return client;
    }

    public void setClient(ClientAggregate client) {
        this.client = client;
    }

    public Set<OrderItem> getItensToExchange() {
        return itensToExchange;
    }

    public void setItensToExchange(Set<OrderItem> itensToExchange) {
        this.itensToExchange = itensToExchange;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public ExchangeStatus getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(ExchangeStatus exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public BigDecimal getExchangeValue() {
        return exchangeValue;
    }

    public void setExchangeValue(BigDecimal exchangeValue) {
        this.exchangeValue = exchangeValue;
    }

    public ExchangeRequestVoucher getExchangeRequestVoucher() {
        return exchangeRequestVoucher;
    }

    public void setExchangeRequestVoucher(ExchangeRequestVoucher exchangeRequestVoucher) {
        this.exchangeRequestVoucher = exchangeRequestVoucher;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public LocalDateTime getItemsReceivedAt() {
        return itemsReceivedAt;
    }

    public void setItemsReceivedAt(LocalDateTime itemsReceivedAt) {
        this.itemsReceivedAt = itemsReceivedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
