package com.gtlab.godjigame.wax.eosioproxyservice.authorization;

import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import java.util.List;
import one.block.eosiojava.models.rpcProvider.Action;

/**
 * The component inserts the defined authorization to actions passed.
 */
public interface AuthorizationFiller {
    /**
     * Get the actions and fill them with predefined authorization.
     *
     * @param actions Action DTOs.
     * @return EOSIO Actions with predefined authorizations.
     */
    List<Action> fillActionsWithAuthorization(List<ActionDto> actions);
}
