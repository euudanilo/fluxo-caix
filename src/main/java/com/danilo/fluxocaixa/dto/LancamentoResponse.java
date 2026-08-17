package com.danilo.fluxocaixa.dto;

import com.danilo.fluxocaixa.domain.LancamentoFinanceiro;
import com.danilo.fluxocaixa.domain.StatusLancamento;
import com.danilo.fluxocaixa.domain.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        TipoLancamento tipo,
        String descricao,
        BigDecimal valorOriginal,
        BigDecimal valorAtualizado,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        StatusLancamento status
) {
    public static LancamentoResponse from(LancamentoFinanceiro lancamento) {
        return new LancamentoResponse(
                lancamento.getId(),
                lancamento.getCliente().getId(),
                lancamento.getCliente().getNome(),
                lancamento.getTipo(),
                lancamento.getDescricao(),
                lancamento.getValorOriginal(),
                lancamento.calcularValorAtualizado(LocalDate.now()),
                lancamento.getDataVencimento(),
                lancamento.getDataPagamento(),
                lancamento.getStatus()
        );
    }
}