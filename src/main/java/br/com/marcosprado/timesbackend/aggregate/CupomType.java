package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.exception.ValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Tipos de cupom disponíveis no sistema
 */
public enum CupomType {

    EXCHANGE_CUPOM("TROCA"),
    DISCOUNT_CUPOM("DESCONTO");

    private final String code;

    CupomType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * Converte string para enum
     * @param value valor em ‘string’ (code ou name)
     * @return CupomType correspondente
     * @throws IllegalArgumentException se valor inválido
     */
    @JsonCreator
    public static CupomType fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tipo de cupom não pode ser nulo ou vazio");
        }

        String normalizedValue = value.trim().toUpperCase();

        for (CupomType type : CupomType.values()) {
            if (type.code.equals(normalizedValue)) {
                return type;
            }
        }

        try {
            return CupomType.valueOf(normalizedValue);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(
                    String.format("Tipo de cupom inválido: '%s'. Valores aceitos: TROCA, DESCONTO, EXCHANGE_CUPOM, DISCOUNT_CUPOM", value)
            );
        }
    }

    public boolean isDiscount() {
        return this == DISCOUNT_CUPOM;
    }

    public boolean isExchange() {
        return this == EXCHANGE_CUPOM;
    }
}
