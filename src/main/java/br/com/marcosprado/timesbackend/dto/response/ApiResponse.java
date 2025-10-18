package br.com.marcosprado.timesbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        ErrorDetails error,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                true,
                "Operação realizada com sucesso",
                data,
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(
                true,
                "Recurso criado com sucesso",
                data,
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(
                false,
                message,
                null,
                new ErrorDetails(errorCode, null),
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(String message, String errorCode, Map<String, String> details) {
        return new ApiResponse<>(
                false,
                message,
                null,
                new ErrorDetails(errorCode, details),
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> validationError(Map<String, String> validationErrors) {
        return new ApiResponse<>(
                false,
                "Erro de validação dos dados enviados",
                null,
                new ErrorDetails("VALIDATION_ERROR", validationErrors),
                LocalDateTime.now()
        );
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
record ErrorDetails(
        String code,
        Map<String, String> details
) {}
