package com.danilo.fluxocaixa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjecaoCaixaResponse(
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal totalAReceber,
        BigDecimal totalAPagar,
        BigDecimal saldoProjetado
) {
}