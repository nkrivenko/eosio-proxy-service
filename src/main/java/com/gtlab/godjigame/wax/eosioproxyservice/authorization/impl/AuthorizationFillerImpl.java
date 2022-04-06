package com.gtlab.godjigame.wax.eosioproxyservice.authorization.impl;

import com.gtlab.godjigame.wax.eosioproxyservice.authorization.AuthorizationFiller;
import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.AbsentActionsException;
import java.util.ArrayList;
import java.util.List;
import one.block.eosiojava.models.rpcProvider.Action;
import one.block.eosiojava.models.rpcProvider.Authorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@code AuthorizationFiller}.
 */
@Component
public class AuthorizationFillerImpl implements AuthorizationFiller {

    private final List<Authorization> authorizations;

    public AuthorizationFillerImpl(@Value("${application.authorization.actor}") final String actor,
                                   @Value("${application.authorization.permission}") final String permission) {
        this.authorizations = List.of(new Authorization(actor, permission));
    }

    @Override
    public List<Action> fillActionsWithAuthorization(List<ActionDto> actions) {
        if (actions == null || actions.isEmpty()) {
            throw new AbsentActionsException();
        }

        List<Action> actionList = new ArrayList<>();

        for (var action : actions) {
            var eosAction = new Action(action.contractAccount(), action.contractMethod(),
                authorizations, action.data().toString());

            actionList.add(eosAction);
        }

        return actionList;
    }
}
