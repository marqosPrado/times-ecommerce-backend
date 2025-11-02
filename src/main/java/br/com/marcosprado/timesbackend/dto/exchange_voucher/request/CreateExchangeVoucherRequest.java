package br.com.marcosprado.timesbackend.dto.exchange_voucher.request;

import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeType;

import java.util.Set;

public record CreateExchangeVoucherRequest(
        long purchaseOrder,
        Set<Long> orderItensId,
        ExchangeType exchangeType
) {
}
