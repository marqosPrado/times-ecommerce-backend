package br.com.marcosprado.timesbackend.aggregate.purchase_order;

import br.com.marcosprado.timesbackend.aggregate.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    private PurchaseOrder purchaseOrder;

    protected OrderItem() {
    }

    public OrderItem(
            Long id,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subTotal,
            Product product
    ) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.product = product;
    }

    public OrderItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
        this.unitPrice = product.getPrice();
        calculateSubTotal();
    }

    private void calculateSubTotal() {
        if (this.unitPrice != null && quantity > 0) {
            this.subTotal = this.unitPrice.multiply(new BigDecimal(quantity));
        }
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    private void setProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Produto não deve ser nulo");
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    private void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantidade do produto deve ser maior que 0");
        this.quantity = quantity;
        calculateSubTotal();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null) throw new IllegalArgumentException("O preço unitário do produto não deve ser nulo");
        this.unitPrice = unitPrice;
        calculateSubTotal();
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity);
    }
}
