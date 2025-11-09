package br.com.marcosprado.timesbackend.dto.exchangeVoucher;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;

public record ExchangeVoucherSummaryResponse(
        Long id,
        String identifier,
        String amount,
        boolean isActive
) {

    public static ExchangeVoucherSummaryResponse fromEntity(ExchangeRequestVoucher voucher) {
        return new ExchangeVoucherSummaryResponse(
                voucher.getId(),
                voucher.getIdentifier(),
                voucher.getAmount().toPlainString(),
                voucher.isActive()
        );
    }
}
