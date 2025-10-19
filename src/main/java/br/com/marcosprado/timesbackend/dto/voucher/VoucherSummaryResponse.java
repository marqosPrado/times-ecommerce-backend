package br.com.marcosprado.timesbackend.dto.voucher;

import br.com.marcosprado.timesbackend.aggregate.Voucher;
import com.fasterxml.jackson.annotation.JsonProperty;

public record VoucherSummaryResponse(
        Long id,

        @JsonProperty("code")
        String code,

        @JsonProperty("discount_percentage")
        String discountPercentage,

        @JsonProperty("type")
        String type
) {

    public static VoucherSummaryResponse fromEntity(Voucher voucher) {
        if (voucher == null) return null;

        return new VoucherSummaryResponse(
                voucher.getId(),
                voucher.getIdentifier(),
                voucher.getPercentage().toPlainString(),
                voucher.getCupomType().getCode()
        );
    }
}
