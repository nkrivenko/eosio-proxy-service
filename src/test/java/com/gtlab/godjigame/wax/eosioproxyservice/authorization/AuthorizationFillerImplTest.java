package com.gtlab.godjigame.wax.eosioproxyservice.authorization;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtlab.godjigame.wax.eosioproxyservice.authorization.impl.AuthorizationFillerImpl;
import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.exceptions.AbsentActionsException;
import com.gtlab.godjigame.wax.eosioproxyservice.generators.DummyStubsGenerator;
import java.util.ArrayList;
import java.util.List;
import one.block.eosiojava.models.rpcProvider.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorizationFillerImplTest {

    @Autowired
    private AuthorizationFillerImpl authorizationFiller;

    @Autowired
    private DummyStubsGenerator dummyStubsGenerator;

    private Authorization authorization;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        final var authorizationField = authorizationFiller.getClass().getDeclaredField("authorizations");
        authorizationField.setAccessible(true);

        final var authorizationsFromFiller = (List<Authorization>) authorizationField.get(authorizationFiller);
        authorization = authorizationsFromFiller.get(0);
    }

    @Test
    void whenFillActionsWithAuthorizationNullActions_thenThrowAbsentActionsException() {
        assertThrows(AbsentActionsException.class, () -> authorizationFiller.fillActionsWithAuthorization(null));
    }

    @Test
    void whenFillActionsWithAuthorizationEmptyActions_thenThrowAbsentActionsException() {
        assertThrows(AbsentActionsException.class, () -> authorizationFiller.fillActionsWithAuthorization(new ArrayList<>()));
    }

    @Test
    void whenFillActionsWithAuthorization_thenReturnListOfActionsWithAuthorization() throws JsonProcessingException {
        final var action = dummyStubsGenerator.createDummyAction();

        final var actionDtos = new ArrayList<ActionDto>();
        actionDtos.add(new ActionDto(action.getAccount(), action.getName(), new ObjectMapper().readTree(action.getData())));

        final var actions = authorizationFiller.fillActionsWithAuthorization(actionDtos);

        assertAll(
            () -> assertEquals(actionDtos.size(), actions.size()),
            () -> assertEquals(authorization, actions.get(0).getAuthorization().get(0))
        );
    }
}
