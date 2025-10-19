package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderItemResponse(
        Long id,

        @JsonProperty("product")
        ProductSummaryResponse product,

        @JsonProperty("quantity")
        Integer quantity,

        @JsonProperty("unit_price")
        String unitPrice,

        @JsonProperty("subtotal")
        String subtotal
) {

    public static OrderItemResponse fromEntity(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                ProductSummaryResponse.fromEntity(item.getProduct()),
                item.getQuantity(),
                item.getUnitPrice().toPlainString(),
                item.getSubTotal().toPlainString()
        );
    }
}
