package com.danilo.fluxocaixa.controller;

import com.danilo.fluxocaixa.dto.ProjecaoCaixaResponse;
import com.danilo.fluxocaixa.service.ProjecaoCaixaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/projecao-caixa")
public class ProjecaoCaixaController {

    private final ProjecaoCaixaService projecaoCaixaService;

    public ProjecaoCaixaController(ProjecaoCaixaService projecaoCaixaService) {
        this.projecaoCaixaService = projecaoCaixaService;
    }

    @GetMapping
    public ResponseEntity<ProjecaoCaixaResponse> projetar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        ProjecaoCaixaResponse projecao = projecaoCaixaService.projetar(dataInicio, dataFim);
        return ResponseEntity.ok(projecao);
    }
}