package com.gtlab.godjigame.wax.eosioproxyservice.exceptions;

import com.gtlab.godjigame.wax.eosioproxyservice.rpc.errors.EosioJavaRpcProviderCallError;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static java.util.Objects.requireNonNull;

/**
 * RuntimeException wrapper for EosioJava RPC errors.
 */
public class EosioJavaRpcException extends EosioException {
    private final EosioJavaRpcProviderCallError error;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public EosioJavaRpcException(EosioJavaRpcProviderCallError error) {
        this.error = requireNonNull(error);
    }

    /**
     * Get the clone of the error.
     *
     * @return Cloned EosioJavaRpcProviderCallError
     */
    public EosioJavaRpcProviderCallError getError() {
        return new EosioJavaRpcProviderCallError(
            error.getMessage(),
            (Exception) error.getCause(),
            error.getRpcResponseError()
        );
    }
}
