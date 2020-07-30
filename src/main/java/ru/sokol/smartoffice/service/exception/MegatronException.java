package ru.sokol.smartoffice.service.exception;

public class MegatronException extends Exception {
    public MegatronException() {
    }

    public MegatronException(String message) {
        super(message);
    }

    public MegatronException(String message, Throwable cause) {
        super(message, cause);
    }

    public MegatronException(Throwable cause) {
        super(cause);
    }

    public MegatronException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
