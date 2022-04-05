package com.gtlab.godjigame.wax.eosioproxyservice.dto;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Action without Authorization.
 * This supposed to be passed as request body to controller.
 *
 * @param contractAccount account of WAX smart contract
 * @param contractMethod action of WAX smart contract
 * @param data JSON data of action parameters
 */
public record ActionDto(String contractAccount, String contractMethod, JsonNode data) {
}
