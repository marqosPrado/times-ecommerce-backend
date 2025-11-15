package br.com.marcosprado.timesbackend.dto.iaRecommendation.request;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;

import java.math.BigDecimal;
import java.util.List;

public class ProductRecommendationRequestBuilder {

    private final ClientAggregate client;
    private final List<PurchaseOrder> purchaseOrders;

    private ProductRecommendationRequestBuilder(
            ClientAggregate client,
            List<PurchaseOrder> purchaseOrders
    ) {
        this.client = client;
        this.purchaseOrders = purchaseOrders != null ? purchaseOrders : List.of();
    }

    public static ProductRecommendationRequestBuilder builder(
            ClientAggregate client,
            List<PurchaseOrder> purchaseOrders
    ) {
        return new ProductRecommendationRequestBuilder(client, purchaseOrders);
    }

    public ProductRecommendationRequest build() {
        return new ProductRecommendationRequest(
                buildCustomerData(),
                buildPurchaseHistory()
        );
    }

    private ProductRecommendationRequest.CustomerData buildCustomerData() {
        var totalSpent = purchaseOrders.stream()
                .map(PurchaseOrder::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ProductRecommendationRequest.CustomerData(
                client.getId(),
                client.getFullName(),
                client.getGender(),
                client.getBirthDate(),
                null,
                client.getCreatedAt(),
                totalSpent,
                purchaseOrders.size()
        );
    }

    private List<ProductRecommendationRequest.PurchaseHistoryData> buildPurchaseHistory() {
        return purchaseOrders.stream()
                .filter(PurchaseOrder::isDelivered)
                .map(this::buildPurchaseHistoryData)
                .toList();
    }

    private ProductRecommendationRequest.PurchaseHistoryData buildPurchaseHistoryData(
            PurchaseOrder order
    ) {
        var items = order.getItems().stream()
                .map(this::buildPurchasedItemData)
                .toList();

        return new ProductRecommendationRequest.PurchaseHistoryData(
                order.getPurchaseOrderNumber(),
                order.getDeliveredAt(),
                order.getTotal(),
                items
        );
    }

    private ProductRecommendationRequest.PurchasedItemData buildPurchasedItemData(
            OrderItem item
    ) {
        var product = item.getProduct();

        return new ProductRecommendationRequest.PurchasedItemData(
                product.getId(),
                product.getBrand(),
                product.getTitle(),
                item.getUnitPrice(),
                item.getQuantity(),
                product.getGender(),
                product.getLine(),
                product.getStyle(),
                product.getMechanism(),
                product.getBoxMaterial(),
                product.getBoxFormat(),
                product.getDial()
        );
    }
}