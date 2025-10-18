package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vouchers", indexes = {
        @Index(name = "idx_voucher_identifier", columnList = "identifier"),
        @Index(name = "idx_voucher_active_dates", columnList = "isActive,startDate,endDate")
})
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vou_id")
    private Long id;

    @Column(length = 6, nullable = false, unique = true, name = "identifier")
    private String identifier;

    @Column(nullable = false, name = "cupom_type")
    @Enumerated(EnumType.STRING)
    private CupomType cupomType;

    @Column(nullable = false, precision = 5, scale = 2, name = "percentage")
    private BigDecimal percentage;

    @Column(nullable = false, name = "start_date")
    private LocalDateTime startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDateTime endDate;

    @Column(nullable = false, name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "voucher",  fetch = FetchType.LAZY)
    private Set<PurchaseOrder> purchaseOrders;

    protected Voucher() {}

    public Voucher(
            Long id,
            String identifier,
            CupomType cupomType,
            BigDecimal percentage,
            LocalDateTime startDate,
            LocalDateTime endDate,
            boolean isActive
    ) {
        this.id = id;
        this.identifier = identifier;
        this.cupomType = cupomType;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Voucher(String identifier, CupomType cupomType, BigDecimal percentage, LocalDateTime endDate) {
        setIdentifier(identifier);
        this.cupomType = cupomType;
        setPercentage(percentage);
        this.startDate = LocalDateTime.now();
        setEndDate(endDate);
        this.isActive = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }

    public boolean isValid() {
        return isActive && !isExpired() && LocalDateTime.now().isAfter(startDate);
    }

    public Long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        if (Objects.isNull(identifier)) {
            throw new IllegalArgumentException("Identificador do cupom é obrigatório.");
        }

        if (identifier.length() != 6) {
            throw new IllegalArgumentException("Identificador do cupom deve ter 6 caracteres.");
        }
        this.identifier = identifier.toUpperCase().trim();
    }

    public CupomType getCupomType() {
        return cupomType;
    }

    public void setCupomType(CupomType cupomType) {
        this.cupomType = cupomType;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        if (Objects.isNull(percentage)) {
            throw new IllegalArgumentException("A porcentagem de desconto do cupom é obrigatória");
        }

        if (percentage.compareTo(BigDecimal.ZERO) <= 0 || percentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("A porcentagem de desconto deve ficar entre 0 e 100 porcento");
        }
        this.percentage = percentage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        if (Objects.isNull(endDate)) {
            throw new IllegalArgumentException("A data de expiração do cupom é obrigatória.");
        }

        if (endDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de expiração do cupom não deve ser no passado.");
        }
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return isActive == voucher.isActive
                && Objects.equals(id, voucher.id)
                && Objects.equals(identifier, voucher.identifier)
                && Objects.equals(percentage, voucher.percentage)
                && Objects.equals(startDate, voucher.startDate)
                && Objects.equals(endDate, voucher.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, percentage, startDate, endDate, isActive);
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", percentage=" + percentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isActive=" + isActive +
                '}';
    }
}
