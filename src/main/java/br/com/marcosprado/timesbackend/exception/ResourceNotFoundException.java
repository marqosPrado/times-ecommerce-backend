package br.com.marcosprado.timesbackend.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException voucherNotFound(String identifier) {
        return new ResourceNotFoundException(
                String.format("Cupom com identificador '%s' não foi encontrado.", identifier)
        );
    }

    public ResourceNotFoundException voucherNotFoundById(Long id) {
        return new ResourceNotFoundException(
                String.format("Cupom com o id '%d' não foi encontrado.", id)
        );
    }
}
