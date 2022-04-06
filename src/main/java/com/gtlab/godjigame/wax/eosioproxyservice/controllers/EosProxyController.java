package com.gtlab.godjigame.wax.eosioproxyservice.controllers;

import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.services.EosProxyService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/chain")
public class EosProxyController {

    private final EosProxyService eosProxyService;

    public EosProxyController(final EosProxyService eosProxyService) {
        this.eosProxyService = eosProxyService;
    }

    @PostMapping("/push_transaction")
    ResponseEntity<?> sendTransaction(@RequestBody final List<ActionDto> actions) {
        final var transactionId = eosProxyService.signAndSendActions(actions);

        final var response = new JSONObject();
        response.put("transactionId", transactionId);
        return ResponseEntity.accepted().body(response.toString());
    }

}
