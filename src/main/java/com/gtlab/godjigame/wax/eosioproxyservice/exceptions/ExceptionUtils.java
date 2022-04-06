package com.gtlab.godjigame.wax.eosioproxyservice.exceptions;

/**
 * Exception traversal utility class.
 */
public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    /**
     * Search the exception with needed type through the chain of exception causes.
     *
     * @param exception exception to start with
     * @param exceptionOfClassToFind class of exception to find
     * @param <TException> type of exception to find
     * @return exception of {@param exceptionOfClassToFind} if there is such exception, <pre>null</pre> otherwise
     */
    public static <TException> TException findFirstExceptionInStackTrace(final Throwable exception, final Class<TException> exceptionOfClassToFind) {
        var traversingException = exception;
        do {
            if (traversingException.getClass().isAssignableFrom(exceptionOfClassToFind)) {
                return (TException) traversingException;
            }

            traversingException = traversingException.getCause();
        } while (traversingException != null);

        return null;
    }
}
