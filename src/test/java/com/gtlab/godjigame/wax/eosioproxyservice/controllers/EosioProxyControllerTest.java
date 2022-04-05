package com.gtlab.godjigame.wax.eosioproxyservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtlab.godjigame.wax.eosioproxyservice.dto.ActionDto;
import com.gtlab.godjigame.wax.eosioproxyservice.services.EosProxyService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EosioProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EosProxyService proxyService;

    @Test
    void whenPostPushTransaction_thenReturnJsonWithTransactionID() throws Exception {
        final var transactionId = "123456";

        final ObjectMapper objectMapper = new ObjectMapper();
        final var body = List.of(new ActionDto("test", "test", objectMapper.createObjectNode()));

        when(proxyService.signAndSendActions(any())).thenReturn(transactionId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/chain/push_transaction")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body))
            ).andExpect(MockMvcResultMatchers.jsonPath("transactionId", Matchers.equalTo(transactionId)));
    }
}
