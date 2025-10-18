package br.com.marcosprado.timesbackend.aggregate.purchase_order;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.aggregate.Voucher;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String purchaseOrderNumber;

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
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id", referencedColumnName = "crd_id")
    private CreditCardAggregate creditCard;
}
