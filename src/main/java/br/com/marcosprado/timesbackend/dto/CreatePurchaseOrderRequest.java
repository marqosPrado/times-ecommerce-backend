package br.com.marcosprado.timesbackend.dto;

import java.util.List;
import java.util.Set;

public record CreatePurchaseOrderRequest(
        List<PurchaseOrderItemDto> orderItem,
        int addressId,
        Set<Integer> creditCardId,
        String voucher
) {
}
