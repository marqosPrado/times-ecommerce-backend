package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PricingResponse(
        @JsonProperty("subtotal")
        String subtotal,

        @JsonProperty("discount")
        String discount,

        @JsonProperty("delivery_fee")
        String deliveryFee,

        @JsonProperty("total")
        String total,

        @JsonProperty("total_items")
        Integer totalItems
) {

    public static PricingResponse from(PurchaseOrder order) {
        int totalItems = order.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        return new PricingResponse(
                order.getSubTotal().toPlainString(),
                order.getDiscount().toPlainString(),
                order.getDeliveryFee().toPlainString(),
                order.getTotal().toPlainString(),
                totalItems
        );
    }
}
