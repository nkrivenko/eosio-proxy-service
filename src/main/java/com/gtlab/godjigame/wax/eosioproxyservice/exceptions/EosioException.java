package com.gtlab.godjigame.wax.eosioproxyservice.exceptions;

/**
 * General RuntimeException wrapper for EosioJava checked exceptions.
 */
public class EosioException extends RuntimeException {
    public EosioException() {
    }

    public EosioException(String message) {
        super(message);
    }

    public EosioException(String message, Throwable cause) {
        super(message, cause);
    }

    public EosioException(Throwable cause) {
        super(cause);
    }
}
