package com.gtlab.godjigame.wax.eosioproxyservice.exceptions;

/**
 * Exception for case when no actions passed.
 */
public class AbsentActionsException extends RuntimeException {
    public AbsentActionsException() {
        super();
    }

    public AbsentActionsException(String message) {
        super(message);
    }

    public AbsentActionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbsentActionsException(Throwable cause) {
        super(cause);
    }
}
