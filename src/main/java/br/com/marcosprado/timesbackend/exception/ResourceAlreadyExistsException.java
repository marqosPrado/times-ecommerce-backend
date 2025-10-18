package br.com.marcosprado.timesbackend.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a resource already exists
 */
public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String message) {
        super(message, "RESOURCE_ALREADY_EXISTS");
    }

    public static ResourceAlreadyExistsException voucherAlreadyExists(String identifier) {
        return new ResourceAlreadyExistsException(
                String.format("Cupom com o identificador '%s' já está cadastrado.", identifier)
        );
    }
}
