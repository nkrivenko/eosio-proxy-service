package com.gtlab.godjigame.wax.eosioproxyservice.services;

import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioException;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.EosioJavaRpcException;
import java.util.List;

/**
 * Service for EOSIO RPC access.
 */
public interface EosProxyService {
    /**
     * Sign actions with provided keys and send them to EOSIO.
     *
     * @param actions list of unsigned actions
     * @return Transaction ID on success
     * @throws EosioJavaRpcException if RPC Error occured
     * @throws EosioException if general exception occured
     */
    String signAndSendActions(List<ActionDto> actions);
}
