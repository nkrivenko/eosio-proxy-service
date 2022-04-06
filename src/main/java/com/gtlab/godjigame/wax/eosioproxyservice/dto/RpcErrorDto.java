package com.gtlab.godjigame.wax.eosioproxyservice.dto;

/**
 * RPC Error representation.
 *
 * @param code EOS code error
 * @param message RPC error message
 * @param name RPC error code
 * @param description RPC error description
 */
public record RpcErrorDto(int code, String message, String name, String description) {
}
