package br.com.marcosprado.timesbackend.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }

    public ValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", cause);
    }

    public static ValidationException invalidPercentage(String details) {
        return new ValidationException("Porcentagem inválida: " + details);
    }

    public static ValidationException expiredVoucher(String identifier) {
        return new ValidationException(
                String.format("Cupom '%s' está expirado e não pode ser utilizado.", identifier)
        );
    }

    public static ValidationException inactiveVoucher(String identifier) {
        return new ValidationException(
                String.format("Cupom '%s' está inativo e não pode ser utilizado.", identifier)
        );
    }
}
