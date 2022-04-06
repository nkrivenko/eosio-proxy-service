package com.gtlab.godjigame.wax.eosioproxyservice.exceptions;

/**
 * Exception for case of transaction preparation failure.
 */
public class TransactionPreparationException extends EosioException {
    public TransactionPreparationException() {
        super();
    }

    public TransactionPreparationException(String message) {
        super(message);
    }

    public TransactionPreparationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionPreparationException(Throwable cause) {
        super(cause);
    }
}
