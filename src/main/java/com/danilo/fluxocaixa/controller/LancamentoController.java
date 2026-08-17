package com.danilo.fluxocaixa.controller;

import com.danilo.fluxocaixa.domain.LancamentoFinanceiro;
import com.danilo.fluxocaixa.dto.CriarLancamentoRequest;
import com.danilo.fluxocaixa.dto.LancamentoResponse;
import com.danilo.fluxocaixa.service.LancamentoFinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private final LancamentoFinanceiroService lancamentoService;

    public LancamentoController(LancamentoFinanceiroService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @PostMapping
    public ResponseEntity<LancamentoResponse> cadastrar(@Valid @RequestBody CriarLancamentoRequest request) {
        LancamentoFinanceiro lancamento = lancamentoService.cadastrar(
                request.clienteId(),
                request.tipo(),
                request.descricao(),
                request.valorOriginal(),
                request.dataVencimento()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(LancamentoResponse.from(lancamento));
    }

    @PatchMapping("/{id}/pagamento")
    public ResponseEntity<LancamentoResponse> registrarPagamento(@PathVariable Long id,
                                                                 @RequestParam(required = false) LocalDate dataPagamento) {
        LocalDate data = dataPagamento != null ? dataPagamento : LocalDate.now();
        LancamentoFinanceiro lancamento = lancamentoService.registrarPagamento(id, data);

        return ResponseEntity.ok(LancamentoResponse.from(lancamento));
    }
}