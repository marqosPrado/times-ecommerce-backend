package br.com.marcosprado.timesbackend.dto.exchange_voucher.response;

import java.math.BigDecimal;

public record ExchangeItemResponse(
        Long orderItemId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
}
