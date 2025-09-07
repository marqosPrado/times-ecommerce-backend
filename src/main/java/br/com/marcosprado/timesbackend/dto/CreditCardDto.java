package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.enums.CardFlag;

public record CreditCardDto(
        Integer id,
        String number,
        String printedName,
        CardFlag cardFlag,
        String securityCode,
        Boolean isMain
) {
    public static CreditCardDto fromEntity(CreditCardAggregate card) {
        return new CreditCardDto(
                card.getId(),
                card.getNumber(),
                card.getPrintedName(),
                card.getCardFlag(),
                card.getSecurityCode(),
                card.getMain()
        );
    }
}
