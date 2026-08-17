package com.danilo.fluxocaixa.dto;

import com.danilo.fluxocaixa.domain.TipoLancamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarLancamentoRequest(
        @NotNull Long clienteId,
        @NotNull TipoLancamento tipo,
        @NotBlank String descricao,
        @NotNull @Positive BigDecimal valorOriginal,
        @NotNull LocalDate dataVencimento
) {
}