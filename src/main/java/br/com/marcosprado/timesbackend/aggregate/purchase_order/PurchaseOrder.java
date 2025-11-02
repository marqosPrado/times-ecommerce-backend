package br.com.marcosprado.timesbackend.aggregate.purchase_order;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.aggregate.Voucher;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 18, nullable = false, unique = true)
    private String purchaseOrderNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "add_id")
    private AddressAggregate address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", referencedColumnName = "vou_id")
    private Voucher voucher;

    @ManyToMany
    @JoinTable(
            name = "credit_card_purchase_order",
            joinColumns = @JoinColumn(name = "purchase_order"),
            inverseJoinColumns = @JoinColumn(name = "credit_card")
    )
    private Set<CreditCardAggregate> creditCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "cli_id")
    private ClientAggregate client;

    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    private Set<ExchangeRequest> exchangeRequests = new HashSet<>();

    public PurchaseOrder() {
    }

    public PurchaseOrder(
            Long id,
            String purchaseOrderNumber,
            OrderStatus orderStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            BigDecimal subTotal,
            BigDecimal discount,
            BigDecimal deliveryFee,
            BigDecimal total,
            Set<OrderItem> items,
            AddressAggregate address,
            Voucher voucher,
            Set<CreditCardAggregate> creditCard,
            ClientAggregate client
    ) {
        this.id = id;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.subTotal = subTotal;
        this.discount = discount;
        this.deliveryFee = deliveryFee;
        this.total = total;
        this.items = items;
        this.address = address;
        this.voucher = voucher;
        this.creditCard = creditCard;
        this.client = client;
    }

    public PurchaseOrder(String purchaseOrderNumber, BigDecimal deliveryFee) {
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.orderStatus = OrderStatus.PROCESSING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.subTotal = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
        this.deliveryFee = deliveryFee;
        this.total = BigDecimal.ZERO;
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não deve ser nulo");
        }
        this.items.add(item);
        item.setPurchaseOrder(this);
        recalculateTotals();
    }

    private void recalculateTotals() {
        this.subTotal = items.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (voucher != null) {
            this.discount = subTotal
                    .multiply(voucher.getPercentage())
                    .divide(new BigDecimal("100"));
        } else {
            this.discount = BigDecimal.ZERO;
        }

        this.total = subTotal.subtract(this.discount).add(deliveryFee);
    }

    public void applyVoucher(Voucher voucher) {
        if (voucher != null) {
            if (voucher.isExpired()) {
                throw new IllegalArgumentException("Cupom expirado!");
            }

            if (!voucher.isValid()) {
                throw new IllegalArgumentException("Cupom inválido!");
            }

            this.voucher = voucher;
            recalculateTotals();
        }
    }

    public boolean isDelivered() {
        return orderStatus == OrderStatus.DELIVERED;
    }

    public Long getId() {
        return id;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public AddressAggregate getAddress() {
        return address;
    }

    public void setAddress(AddressAggregate address) {
        if (address == null) {
            throw new IllegalArgumentException("Endereço não deve ser nulo");
        }
        this.address = address;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Set<CreditCardAggregate> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Set<CreditCardAggregate> creditCard) {
        this.creditCard = creditCard;
    }

    public ClientAggregate getClient() {
        return client;
    }

    public void setClient(ClientAggregate client) {
        if (client == null) throw new IllegalArgumentException("Cliente não deve ser nulo");
        this.client = client;
    }
}
