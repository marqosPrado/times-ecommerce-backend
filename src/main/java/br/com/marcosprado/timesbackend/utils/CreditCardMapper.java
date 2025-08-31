package br.com.marcosprado.timesbackend.utils;

import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import br.com.marcosprado.timesbackend.domain.CreditCard;

import java.util.List;
import java.util.stream.Collectors;

public class CreditCardMapper {

    public static CreditCardAggregate toAggregate(CreditCard card) {
        return new CreditCardAggregate(
                card.getNumber(),
                card.getPrintedName(),
                card.getFlag(),
                card.getSecurityCode()
        );
    }

    public static List<CreditCardAggregate> toAggregate(List<CreditCard> cards) {
        return cards.stream()
                .map(CreditCardMapper::toAggregate)
                .collect(Collectors.toList());
    }
}
