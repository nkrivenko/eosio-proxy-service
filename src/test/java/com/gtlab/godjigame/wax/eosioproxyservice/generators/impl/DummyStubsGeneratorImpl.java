package com.gtlab.godjigame.wax.eosioproxyservice.generators.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.gtlab.godjigame.wax.eosioproxyservice.generators.DummyStubsGenerator;
import one.block.eosiojava.models.rpcProvider.Action;
import one.block.eosiojava.models.rpcProvider.Authorization;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DummyStubsGeneratorImpl implements DummyStubsGenerator {
    @Override
    public Action createDummyAction() {
        final var authorizations = new ArrayList<Authorization>();
        authorizations.add(createDummyAuthorization());

        final var data = JsonNodeFactory.instance.objectNode().put("from", "me")
            .put("to", "others");

        return new Action("contract", "action", authorizations, data.toString());
    }

    private Authorization createDummyAuthorization() {
        return new Authorization("dummy", "active");
    }
}
