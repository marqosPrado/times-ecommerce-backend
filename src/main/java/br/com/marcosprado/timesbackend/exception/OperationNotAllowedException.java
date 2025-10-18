package br.com.marcosprado.timesbackend.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when operation is not allowed
 */
public class OperationNotAllowedException extends BusinessException {
    public OperationNotAllowedException(String message) {
        super(message, "OPERATION_NOT_ALLOWED");
    }

    public static OperationNotAllowedException cannotActivateExpiredVoucher(String identifier) {
        return new OperationNotAllowedException(
                String.format("Não é possível ativar o cupom '%s' pois está expirado.", identifier)
        );
    }

    public static OperationNotAllowedException cannotUpdateExpiredVoucher(String identifier) {
        return new OperationNotAllowedException(
                String.format("Não é possível atualizar o cupom '%s' pois está expirado.", identifier)
        );
    }
}
