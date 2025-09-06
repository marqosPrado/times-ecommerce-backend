package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.enums.CardFlag;

public record CreditCardDto(
        String number,
        String printedName,
        CardFlag cardFlag,
        String securityCode
) {
}
