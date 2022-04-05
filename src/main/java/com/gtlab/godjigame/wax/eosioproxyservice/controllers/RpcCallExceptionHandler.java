package com.gtlab.godjigame.wax.eosioproxyservice.controllers;

import com.gtlab.godjigame.wax.eosioproxyservice.dto.RpcErrorDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioJavaRpcException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RpcCallExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EosioJavaRpcException.class)
    protected ResponseEntity<?> handleException(EosioJavaRpcException ex, WebRequest request) {
        final var error = ex.getError().getRpcResponseError();
        final var errorCode = error.getCode().intValue();

        final var rpcErrorDto = new RpcErrorDto(error.getError().getCode().intValue(), error.getMessage(),
            error.getError().getName(), error.getError().getWhat());

        return handleExceptionInternal(ex, rpcErrorDto, new HttpHeaders(), HttpStatus.resolve(errorCode), request);
    }

}
