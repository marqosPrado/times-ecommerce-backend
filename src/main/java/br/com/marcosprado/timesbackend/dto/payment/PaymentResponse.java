package br.com.marcosprado.timesbackend.dto.payment;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.dto.credit_card.CreditCardSummaryResponse;
import br.com.marcosprado.timesbackend.dto.voucher.VoucherSummaryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentResponse(
        @JsonProperty("credit_card")
        CreditCardSummaryResponse creditCard,

        @JsonProperty("voucher")
        VoucherSummaryResponse voucher
) {

    public static PaymentResponse from(PurchaseOrder order) {
        return new PaymentResponse(
                CreditCardSummaryResponse.fromEntity(order.getCreditCard()),
                order.getVoucher() != null
                        ? VoucherSummaryResponse.fromEntity(order.getVoucher())
                        : null
        );
    }
}
