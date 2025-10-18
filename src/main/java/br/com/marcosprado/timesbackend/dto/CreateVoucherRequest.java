package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.Voucher;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateVoucherRequest(
        @NotBlank(message = "Identificador do cupom é obrigatório")
        @Size(min = 6, max = 6, message = "O Identificador deve ter exatamente 6 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "O identificador deve compor de letras e números.")
        String identifier,

        @NotNull(message = "A porcentagem do cupom é obrigatória.")
        @Pattern(regexp = "^\\d{1,2}(\\.\\d{1,2})?$", message = "Porcentagem inválida.")
        String percentage,

        @NotNull(message = "Data de expiração do cupom é obrigatória.")
        @Future(message = "Data de expiração do cupom deve ser no futuro.")
        LocalDateTime endDate
) {

    @JsonCreator
    public CreateVoucherRequest(
            @JsonProperty("identifier") String identifier,
            @JsonProperty("percentage") String percentage,
            @JsonProperty("end_date")   LocalDateTime endDate
    ) {
        this.identifier = identifier;
        this.percentage = percentage;
        this.endDate = endDate;
    }

    /**
     * Converts the percentage string to BigDecimal with proper validation
     * @return BigDecimal representation of the percentage
     * @throws IllegalArgumentException if percentage is invalid
     */
    private BigDecimal getPercentageAsBigDecimal() {
        try {
            BigDecimal value = new BigDecimal(percentage);

            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Porcentagem de desconto do cupom deve ser maior que 0%.");
            }

            if (value.compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException("Porcentagem de desconto do cupom não deve exceder 100%");
            }

            if (value.scale() > 2) {
                throw new IllegalArgumentException("Porcentagem de desconto do cupom não deve ter mais que duas casas decimais.");
            }

            return value.setScale(2, BigDecimal.ROUND_HALF_UP);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato da porcentagem de desconto inválido: " + percentage, e);
        }
    }

    public Voucher toEntity() {
        return new Voucher(
                identifier(),
                getPercentageAsBigDecimal(),
                endDate()
        );
    }
}
