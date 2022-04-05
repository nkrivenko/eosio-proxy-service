package com.gtlab.godjigame.wax.eosioproxyservice.configuration;

import com.gtlab.godjigame.wax.eosioproxyservice.rpc.errors.EosioJavaRpcProviderInitializerError;
import com.gtlab.godjigame.wax.eosioproxyservice.rpc.impl.EosioJavaRpcProviderImpl;
import one.block.eosiojava.error.serializationProvider.SerializationProviderError;
import one.block.eosiojava.implementations.ABIProviderImpl;
import one.block.eosiojava.interfaces.IABIProvider;
import one.block.eosiojava.interfaces.IRPCProvider;
import one.block.eosiojava.interfaces.ISerializationProvider;
import one.block.eosiojava.interfaces.ISignatureProvider;
import one.block.eosiojava.session.TransactionProcessor;
import one.block.eosiojava.session.TransactionSession;
import one.block.eosiojavaabieosserializationprovider.AbiEosSerializationProviderImpl;
import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;
import one.block.eosiosoftkeysignatureprovider.error.ImportKeyError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.lang.NonNull;
import org.springframework.web.context.WebApplicationContext;

/**
 * EOSIO RPC configuration.
 */
@Configuration
public class EosioConfiguration {

    @Bean
    IRPCProvider rpcProvider(@Value("${application.eosio.rpc.endpoint}") @NonNull String endpoint) throws EosioJavaRpcProviderInitializerError {
        return new EosioJavaRpcProviderImpl(endpoint);
    }

    @Bean
    ISerializationProvider serializationProvider() throws SerializationProviderError {
        return new AbiEosSerializationProviderImpl();
    }

    @Bean
    IABIProvider abiProvider(final IRPCProvider rpcProvider, final ISerializationProvider serializationProvider) {
        return new ABIProviderImpl(rpcProvider, serializationProvider);
    }

    @Bean
    ISignatureProvider signatureProvider() throws ImportKeyError {
        final SoftKeySignatureProviderImpl softKeySignatureProvider = new SoftKeySignatureProviderImpl();
        softKeySignatureProvider.importKey("5KMj4F4dn58pcmA5grUe3Awv7D8doYEhKT2QLd2PHuN8ruB4Mdf");
        return softKeySignatureProvider;
    }

    @Bean
    TransactionSession transactionSession(final ISerializationProvider serializationProvider,
                                          final IRPCProvider rpcProvider, final IABIProvider abiProvider,
                                          final ISignatureProvider signatureProvider) {
        return new TransactionSession(serializationProvider, rpcProvider, abiProvider, signatureProvider);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    TransactionProcessor transactionProcessor(final TransactionSession transactionSession) {
        return transactionSession.getTransactionProcessor();
    }
}
