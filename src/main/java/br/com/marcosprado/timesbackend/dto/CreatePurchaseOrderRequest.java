package br.com.marcosprado.timesbackend.dto;

import java.util.List;

public record CreatePurchaseOrderRequest(
        List<PurchaseOrderItemDto> orderItem,
        int addressId,
        int creditCardId,
        String voucher
) {
}
