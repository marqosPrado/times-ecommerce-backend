package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "EXCHANGE_REQUEST_VOUCHERS")
public class ExchangeRequestVoucher {

    @Transient
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transient
    private static final int VOUCHER_LENGTH = 6;

    @Transient
    private static final SecureRandom RANDOM = new SecureRandom();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 6, nullable = false, unique = true)
    private String identifier;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientAggregate client;

    @ManyToMany(mappedBy = "exchangeVouchersRequest")
    private Set<PurchaseOrder> purchaseOrders;

    protected ExchangeRequestVoucher() {
    }

    public ExchangeRequestVoucher(
            BigDecimal amount,
            ClientAggregate client
    ) {
        this.identifier = generateIdentifier();
        this.setAmount(amount);
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.client = client;
    }

    private String generateIdentifier() {
        StringBuilder sb = new StringBuilder(VOUCHER_LENGTH);
        for (int i = 0; i < VOUCHER_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public void unableVoucher() {
        if (!isActive) {
            throw new IllegalArgumentException("Cupom de troca já está desativado");
        }
        this.isActive = false;
    }

    public void activeVoucher() {
        if (isActive) {
            throw new IllegalArgumentException("Cupom de troca já está ativo");
        }
    }

    public Long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Valor do cupom deve ser maior que zero");
        }
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ClientAggregate getClient() {
        return client;
    }

    public void setClient(ClientAggregate client) {
        this.client = client;
    }

    public void disable() {
        this.setActive(false);
    }
}
