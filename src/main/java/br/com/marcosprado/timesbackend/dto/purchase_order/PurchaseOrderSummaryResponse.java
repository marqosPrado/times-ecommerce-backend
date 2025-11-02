package br.com.marcosprado.timesbackend.dto.purchase_order;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.OrderStatusResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record PurchaseOrderSummaryResponse(
        Long id,

        @JsonProperty("order_number")
        String orderNumber,

        @JsonProperty("order_status")
        OrderStatusResponse orderStatus,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("updated_at")
        LocalDateTime updatedAt,

        @JsonProperty("client_name")
        String clientName
) {
    public static PurchaseOrderSummaryResponse fromEntity(PurchaseOrder entity) {
        return new PurchaseOrderSummaryResponse(
                entity.getId(),
                entity.getPurchaseOrderNumber(),
                OrderStatusResponse.from(entity.getOrderStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getClient().getFullName()

        );
    }
}
