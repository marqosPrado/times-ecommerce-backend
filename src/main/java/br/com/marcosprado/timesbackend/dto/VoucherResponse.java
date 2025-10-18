package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.CupomType;
import br.com.marcosprado.timesbackend.aggregate.Voucher;

import java.time.LocalDateTime;

public record VoucherResponse(
        Long id,
        String identifier,
        CupomType voucherType,
        String percentage,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isActive,
        boolean isValid
) {

    public static VoucherResponse fromEntity(Voucher voucher) {
        return new VoucherResponse(
                voucher.getId(),
                voucher.getIdentifier(),
                voucher.getCupomType(),
                voucher.getPercentage().toPlainString(),
                voucher.getStartDate(),
                voucher.getEndDate(),
                voucher.isActive(),
                voucher.isValid()
        );
    }
}
