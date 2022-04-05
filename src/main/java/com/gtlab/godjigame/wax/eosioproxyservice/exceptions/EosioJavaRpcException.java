package com.gtlab.godjigame.wax.eosioproxyservice.exceptions;

import com.gtlab.godjigame.wax.eosioproxyservice.rpc.errors.EosioJavaRpcProviderCallError;

import static java.util.Objects.requireNonNull;

/**
 * RuntimeException wrapper for EosioJava RPC errors.
 */
public class EosioJavaRpcException extends EosioException {
    private final EosioJavaRpcProviderCallError error;

    public EosioJavaRpcException(EosioJavaRpcProviderCallError error) {
        this.error = requireNonNull(error);
    }

    /**
     * Get the clone of the error.
     *
     * @return Cloned EosioJavaRpcProviderCallError
     */
    public EosioJavaRpcProviderCallError getError() {
        return error;
    }
}
