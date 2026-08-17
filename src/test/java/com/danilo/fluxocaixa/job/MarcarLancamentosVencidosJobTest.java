package com.danilo.fluxocaixa.job;

import com.danilo.fluxocaixa.domain.Cliente;
import com.danilo.fluxocaixa.domain.ContaAReceber;
import com.danilo.fluxocaixa.domain.LancamentoFinanceiro;
import com.danilo.fluxocaixa.domain.StatusLancamento;
import com.danilo.fluxocaixa.repository.LancamentoFinanceiroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarcarLancamentosVencidosJobTest {

    @Mock
    private LancamentoFinanceiroRepository lancamentoRepository;

    private final Cliente cliente = new Cliente("Joao Silva", "12345678900", "joao@email.com", "31999999999");

    @Test
    void deveMarcarLancamentosPendentesVencidosComoVencido() {
        ContaAReceber lancamentoVencido = new ContaAReceber(
                cliente, "Servico atrasado", new BigDecimal("500.00"), LocalDate.now().minusDays(5)
        );

        when(lancamentoRepository.findByStatusAndDataVencimentoBefore(eq(StatusLancamento.PENDENTE), any(LocalDate.class)))
                .thenReturn(List.of(lancamentoVencido));

        var job = new MarcarLancamentosVencidosJob(lancamentoRepository);
        job.executar();

        assertThat(lancamentoVencido.getStatus()).isEqualTo(StatusLancamento.VENCIDO);
        verify(lancamentoRepository, times(1)).saveAll(List.of(lancamentoVencido));
    }

    @Test
    void naoDeveFazerNadaQuandoNaoHaLancamentosVencidos() {
        when(lancamentoRepository.findByStatusAndDataVencimentoBefore(eq(StatusLancamento.PENDENTE), any(LocalDate.class)))
                .thenReturn(List.of());

        var job = new MarcarLancamentosVencidosJob(lancamentoRepository);
        job.executar();

        verify(lancamentoRepository, never()).saveAll(any());
    }
}