package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.enums.CardFlag;

import java.util.Collection;

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

    public static CreditCardDto[] fromEntities(Collection<CreditCardAggregate> cards) {
        return cards.stream()
                .map(CreditCardDto::fromEntity)
                .toArray(CreditCardDto[]::new);
    }

    public CreditCardAggregate toEntity() {
        return new CreditCardAggregate(
                this.number,
                this.printedName,
                this.cardFlag,
                this.securityCode,
                this.isMain != null ? this.isMain : false
        );
    }
}
