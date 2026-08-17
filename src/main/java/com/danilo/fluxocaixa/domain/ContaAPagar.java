package com.danilo.fluxocaixa.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("PAGAR")
public class ContaAPagar extends LancamentoFinanceiro {

    protected ContaAPagar() {
    }

    public ContaAPagar(Cliente cliente, String descricao, BigDecimal valorOriginal, LocalDate dataVencimento) {
        super(cliente, descricao, valorOriginal, dataVencimento);
    }

    @Override
    public TipoLancamento getTipo() {
        return TipoLancamento.PAGAR;
    }
}