package com.danilo.fluxocaixa.controller;

import com.danilo.fluxocaixa.dto.CriarClienteRequest;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@Transactional
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @Test
    void deveCadastrarClienteComDadosValidos() throws Exception {
        var request = new CriarClienteRequest("Ana Costa", "11122233344", "ana@email.com", "31977777777");

        mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Ana Costa"))
                .andExpect(jsonPath("$.documento").value("11122233344"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deveRetornar400QuandoDocumentoNaoInformado() throws Exception {
        var request = new CriarClienteRequest("Ana Costa", "", "ana@email.com", "31977777777");

        mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoBuscarClienteInexistente() throws Exception {
        mockMvc.perform(get("/api/clientes/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Cliente nao encontrado"));
    }
}