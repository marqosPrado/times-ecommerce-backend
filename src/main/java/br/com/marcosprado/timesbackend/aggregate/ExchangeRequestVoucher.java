package br.com.marcosprado.timesbackend.aggregate;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

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

    protected ExchangeRequestVoucher() {
    }

    public ExchangeRequestVoucher(
            BigDecimal amount
    ) {
        this.identifier = generateIdentifier();
        this.setAmount(amount);
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
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
}
