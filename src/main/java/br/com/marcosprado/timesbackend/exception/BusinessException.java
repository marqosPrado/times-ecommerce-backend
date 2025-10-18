package br.com.marcosprado.timesbackend.exception;


public abstract class BusinessException extends RuntimeException {
    private final String errorCode;

    protected BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
