package br.com.marcosprado.timesbackend.dto.payment;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.credit_card.CreditCardSummaryResponse;
import br.com.marcosprado.timesbackend.dto.voucher.VoucherSummaryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.stream.Collectors;

public record PaymentResponse(
        @JsonProperty("credit_card")
        Set<CreditCardSummaryResponse> creditCard,

        @JsonProperty("voucher")
        VoucherSummaryResponse voucher
) {

    public static PaymentResponse from(PurchaseOrder order) {
        Set<CreditCardSummaryResponse> creditCards = order.getCreditCard() != null
                ? order.getCreditCard().stream()
                .map(CreditCardSummaryResponse::fromEntity)
                .collect(Collectors.toSet())
                : Set.of();

        VoucherSummaryResponse voucherResponse = order.getVoucher() != null
                ? VoucherSummaryResponse.fromEntity(order.getVoucher())
                : null;

        return new PaymentResponse(creditCards, voucherResponse);
    }
}
