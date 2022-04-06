package com.gtlab.godjigame.wax.eosioproxyservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioJavaRpcException;
import com.gtlab.godjigame.wax.eosioproxyservice.rpc.errors.EosioJavaRpcProviderCallError;
import com.gtlab.godjigame.wax.eosioproxyservice.services.EosProxyService;
import one.block.eosiojava.models.rpcProvider.response.RPCResponseError;
import one.block.eosiojava.models.rpcProvider.response.RpcError;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EosioProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EosProxyService proxyService;

    @Test
    void whenPostPushTransaction_thenReturnJsonWithTransactionID() throws Exception {
        final var transactionId = "123456";

        final ObjectMapper objectMapper = new ObjectMapper();
        final var body = List.of(new ActionDto("test", "test", objectMapper.createObjectNode()));

        when(proxyService.signAndSendActions(any())).thenReturn(transactionId);

        mockMvc.perform(post("/v1/chain/push_transaction").with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body))
        ).andExpect(jsonPath("transactionId", Matchers.equalTo(transactionId)));
    }

    @Test
    void whenPostPushTransactionConflict_thenReturnError() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final var body = List.of(new ActionDto("test", "test", objectMapper.createObjectNode()));

        final var rpcResponseError = fillTheError(new RPCResponseError());
        final var rpcException = new EosioJavaRpcProviderCallError("Error", rpcResponseError);
        final var exception = new EosioJavaRpcException(rpcException);

        when(proxyService.signAndSendActions(any())).thenThrow(exception);

        mockMvc.perform(post("/v1/chain/push_transaction").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body))
            ).andExpect(status().isConflict())
            .andExpect(jsonPath("message", Matchers.equalTo(rpcResponseError.getMessage())))
            .andExpect(jsonPath("name", Matchers.equalTo(rpcResponseError.getError().getName())))
            .andExpect(jsonPath("description", Matchers.equalTo(rpcResponseError.getError().getWhat())))
            .andExpect(jsonPath("code", Matchers.equalTo(rpcResponseError.getError().getCode().intValue())));
    }

    private RPCResponseError fillTheError(final RPCResponseError error) {
        try {
            setFieldThroughReflection("code", BigInteger.valueOf(409L), error);
            setFieldThroughReflection("message", "Conflict", error);

            final var rpcError = new RpcError();
            setFieldThroughReflection("code", error.getCode(), rpcError);
            setFieldThroughReflection("name", "tx_duplicate", rpcError);
            setFieldThroughReflection("what", "Conflict", rpcError);

            setFieldThroughReflection("error", rpcError, error);

            return error;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T, Err> void setFieldThroughReflection(final String fieldName, final T fieldValue, final Err error) throws NoSuchFieldException, IllegalAccessException {
        final var errorClazz = error.getClass();

        final var field = errorClazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(error, fieldValue);
    }
}
