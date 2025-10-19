package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.payment.PaymentResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderResponse(
        Long id,

        @JsonProperty("order_number")
        String orderNumber,

        @JsonProperty("order_status")
        OrderStatusResponse orderStatus,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("updated_at")
        LocalDateTime updatedAt,

        @JsonProperty("items")
        List<OrderItemResponse> items,

        @JsonProperty("shipping_address")
        ShippingAddressResponse shippingAddress,

        @JsonProperty("payment")
        PaymentResponse payment,

        @JsonProperty("pricing")
        PricingResponse pricing
) {

    public static PurchaseOrderResponse fromEntity(PurchaseOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("PurchaseOrder não pode ser null");
        }

        return new PurchaseOrderResponse(
                order.getId(),
                order.getPurchaseOrderNumber(),
                OrderStatusResponse.from(order.getOrderStatus()),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getItems().stream()
                        .map(OrderItemResponse::fromEntity)
                        .toList(),
                ShippingAddressResponse.fromEntity(order.getAddress()),
                PaymentResponse.from(order),
                PricingResponse.from(order)
        );
    }
}
