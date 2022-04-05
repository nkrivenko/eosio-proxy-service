package com.gtlab.godjigame.wax.eosioproxyservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.AbsentActionsException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioJavaRpcException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.TransactionPreparationException;
import com.gtlab.godjigame.wax.eosioproxyservice.generators.DummyStubsGenerator;
import com.gtlab.godjigame.wax.eosioproxyservice.rpc.errors.EosioJavaRpcProviderCallError;
import one.block.eosiojava.error.session.TransactionPrepareError;
import one.block.eosiojava.error.session.TransactionSignAndBroadCastError;
import one.block.eosiojava.models.rpcProvider.response.SendTransactionResponse;
import one.block.eosiojava.session.TransactionProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("EOSIO Proxy Service Test")
public class EosProxyServiceImplTest {

    @Autowired
    private EosProxyService eosProxyService;

    @Autowired
    private DummyStubsGenerator dummyStubsGenerator;

    @MockBean
    private TransactionProcessor transactionProcessor;

    @Test
    void whenSignAndSendActionsWithNullActions_thenThrowAbsentActionsException() {
        assertThrows(AbsentActionsException.class, () -> eosProxyService.signAndSendActions(null));
    }

    @Test
    void whenSignAndSendActionsWithEmptyActions_thenThrowAbsentActionsException() {
        assertThrows(AbsentActionsException.class, () -> eosProxyService.signAndSendActions(new ArrayList<>()));
    }

    @Test
    void whenSignAndSendActionsCannotPrepare_thenThrowTransactionPreparationException() throws TransactionPrepareError, JsonProcessingException {
        final var dummyAction = dummyStubsGenerator.createDummyAction();

        doThrow(new TransactionPrepareError()).when(transactionProcessor).prepare(Mockito.any());

        final var actionDto = new ActionDto(dummyAction.getAccount(), dummyAction.getName(), new ObjectMapper().readTree(dummyAction.getData()));
        assertThrows(TransactionPreparationException.class, () -> eosProxyService.signAndSendActions(List.of(actionDto)));
    }

    @Test
    void whenSignAndSendActionsRpcError_thenThrowEosioJavaRpcException() throws TransactionSignAndBroadCastError, JsonProcessingException {
        var exception = new TransactionSignAndBroadCastError(new EosioJavaRpcProviderCallError());

        doThrow(exception).when(transactionProcessor).signAndBroadcast();

        final var dummyAction = dummyStubsGenerator.createDummyAction();
        final var actionDto = new ActionDto(dummyAction.getAccount(), dummyAction.getName(), new ObjectMapper().readTree(dummyAction.getData()));
        assertThrows(EosioJavaRpcException.class, () -> eosProxyService.signAndSendActions(List.of(actionDto)));
    }

    @Test
    void whenSignAndSendActionsGeneralException_thenThrowEosioException() throws TransactionSignAndBroadCastError, JsonProcessingException {
        var exception = new TransactionSignAndBroadCastError();

        doThrow(exception).when(transactionProcessor).signAndBroadcast();

        final var dummyAction = dummyStubsGenerator.createDummyAction();
        final var actionDto = new ActionDto(dummyAction.getAccount(), dummyAction.getName(), new ObjectMapper().readTree(dummyAction.getData()));
        assertThrows(EosioException.class, () -> eosProxyService.signAndSendActions(List.of(actionDto)));
    }

    @Test
    void whenSignAndSendActions_thenReturnTransactionId() throws NoSuchFieldException, TransactionSignAndBroadCastError, IllegalAccessException, JsonProcessingException {
        final var txId = "123456";
        var response = new SendTransactionResponse();

        var txIdField = response.getClass().getDeclaredField("transactionId");
        txIdField.setAccessible(true);
        txIdField.set(response, txId);

        when(transactionProcessor.signAndBroadcast()).thenReturn(response);

        final var dummyAction = dummyStubsGenerator.createDummyAction();
        final var actionDto = new ActionDto(dummyAction.getAccount(), dummyAction.getName(), new ObjectMapper().readTree(dummyAction.getData()));

        var actualTxId = eosProxyService.signAndSendActions(List.of(actionDto));

        assertEquals(txId, actualTxId);
    }
}
