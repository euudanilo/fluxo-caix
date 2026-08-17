package com.danilo.fluxocaixa.service;

import com.danilo.fluxocaixa.domain.ContaAPagar;
import com.danilo.fluxocaixa.domain.ContaAReceber;
import com.danilo.fluxocaixa.dto.ProjecaoCaixaResponse;
import com.danilo.fluxocaixa.repository.LancamentoFinanceiroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProjecaoCaixaService {

    private final LancamentoFinanceiroRepository lancamentoRepository;

    public ProjecaoCaixaService(LancamentoFinanceiroRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    public ProjecaoCaixaResponse projetar(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            throw new PeriodoInvalidoException(dataInicio, dataFim);
        }

        var totalAReceber = lancamentoRepository.somarValorPorTipoEPeriodo(
                ContaAReceber.class, dataInicio, dataFim
        );

        var totalAPagar = lancamentoRepository.somarValorPorTipoEPeriodo(
                ContaAPagar.class, dataInicio, dataFim
        );

        var saldoProjetado = totalAReceber.subtract(totalAPagar);

        return new ProjecaoCaixaResponse(dataInicio, dataFim, totalAReceber, totalAPagar, saldoProjetado);
    }
}