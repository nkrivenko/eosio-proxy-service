package com.gtlab.godjigame.wax.eosioproxyservice.controllers;

import com.gtlab.godjigame.wax.eosioproxyservice.dto.RpcErrorDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioJavaRpcException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.requireNonNull;

@ControllerAdvice
public class RpcCallExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EosioJavaRpcException.class)
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    protected ResponseEntity<?> handleException(EosioJavaRpcException ex, WebRequest request) {
        final var error = requireNonNull(requireNonNull(ex.getError()).getRpcResponseError());
        final var errorCode = error.getCode().intValue();

        final var rpcErrorDto = new RpcErrorDto(error.getError().getCode().intValue(), error.getMessage(),
            error.getError().getName(), error.getError().getWhat());

        final HttpStatus status = HttpStatus.resolve(errorCode);
        if (status == null) {
            throw new RuntimeException();
        }

        return handleExceptionInternal(ex, rpcErrorDto, new HttpHeaders(), status, request);
    }

}
