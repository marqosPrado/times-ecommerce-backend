package br.com.marcosprado.timesbackend.dto;

public record PurchaseOrderItemDto(
        Long productId,
        Integer quantity
) {
}
