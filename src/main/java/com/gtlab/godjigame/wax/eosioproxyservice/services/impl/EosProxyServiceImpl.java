package com.gtlab.godjigame.wax.eosioproxyservice.services.impl;

import com.gtlab.godjigame.wax.eosioproxyservice.authorization.AuthorizationFiller;
import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.AbsentActionsException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioJavaRpcException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.ExceptionUtils;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.TransactionPreparationException;
import com.gtlab.godjigame.wax.eosioproxyservice.rpc.errors.EosioJavaRpcProviderCallError;
import com.gtlab.godjigame.wax.eosioproxyservice.services.EosProxyService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import one.block.eosiojava.error.session.TransactionPrepareError;
import one.block.eosiojava.error.session.TransactionSignAndBroadCastError;
import one.block.eosiojava.session.TransactionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@code EosProxyService}.
 */
@Service
public class EosProxyServiceImpl implements EosProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EosProxyServiceImpl.class);

    private final TransactionProcessor transactionProcessor;
    private final AuthorizationFiller authorizationFiller;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public EosProxyServiceImpl(final TransactionProcessor transactionProcessor,
                               final AuthorizationFiller authorizationFiller) {
        this.transactionProcessor = transactionProcessor;
        this.authorizationFiller = authorizationFiller;
    }

    @Override
    public String signAndSendActions(final List<ActionDto> actions) {
        if (actions == null || actions.isEmpty()) {
            throw new AbsentActionsException();
        }

        final var actionList = authorizationFiller.fillActionsWithAuthorization(actions);

        try {
            transactionProcessor.prepare(actionList);

            final var sendTransactionResponse = transactionProcessor.signAndBroadcast();
            return sendTransactionResponse.getTransactionId();
        } catch (final TransactionPrepareError ex) {
            LOGGER.error("Could not prepare transaction", ex);
            throw new TransactionPreparationException(ex);
        } catch (final TransactionSignAndBroadCastError ex) {
            LOGGER.error("Could not send transaction", ex);
            throw createEosException(ex);
        }
    }

    private RuntimeException createEosException(final TransactionSignAndBroadCastError ex) {
        var rpcError = ExceptionUtils.findFirstExceptionInStackTrace(
            ex, EosioJavaRpcProviderCallError.class
        );

        return rpcError != null ? new EosioJavaRpcException(rpcError) : new EosioException(ex);
    }
}
