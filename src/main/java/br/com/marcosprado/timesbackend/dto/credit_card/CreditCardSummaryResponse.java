package br.com.marcosprado.timesbackend.dto.credit_card;

import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreditCardSummaryResponse(
        Integer id,

        @JsonProperty("card_flag")
        String cardFlag,

        @JsonProperty("last_digits")
        String lastDigits,

        @JsonProperty("printed_name")
        String printedName
) {

    public static CreditCardSummaryResponse fromEntity(CreditCardAggregate card) {
        if (card == null) return null;

        String lastDigits = card.getNumber().length() >= 4
                ? card.getNumber().substring(card.getNumber().length() - 4)
                : card.getNumber();

        return new CreditCardSummaryResponse(
                card.getId(),
                card.getCardFlag().name(),
                lastDigits,
                card.getPrintedName()
        );
    }
}
