package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;

public record OrderStatusResponse(
        String code,
        String displayName,
        String description
) {
    public static OrderStatusResponse from(OrderStatus status) {
        return new OrderStatusResponse(
                status.name(),
                status.getDisplayName(),
                status.getDescription()
        );
    }
}
