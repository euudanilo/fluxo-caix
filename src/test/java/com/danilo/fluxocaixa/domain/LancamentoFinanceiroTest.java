package com.danilo.fluxocaixa.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class LancamentoFinanceiroTest {

    private final Cliente cliente = new Cliente("Joao Silva", "12345678900", "joao@email.com", "31999999999");

    @Test
    void naoDeveAplicarMultaOuJurosQuandoPagoNoVencimento() {
        ContaAReceber lancamento = new ContaAReceber(
                cliente, "Servico prestado", new BigDecimal("1000.00"), LocalDate.of(2026, 1, 10)
        );

        BigDecimal valorAtualizado = lancamento.calcularValorAtualizado(LocalDate.of(2026, 1, 10));

        assertThat(valorAtualizado).isEqualByComparingTo("1000.00");
    }

    @Test
    void naoDeveAplicarMultaOuJurosAntesDoVencimento() {
        ContaAReceber lancamento = new ContaAReceber(
                cliente, "Servico prestado", new BigDecimal("1000.00"), LocalDate.of(2026, 1, 10)
        );

        BigDecimal valorAtualizado = lancamento.calcularValorAtualizado(LocalDate.of(2026, 1, 5));

        assertThat(valorAtualizado).isEqualByComparingTo("1000.00");
    }

    @Test
    void deveAplicarMultaEJurosQuandoPagoComAtraso() {
        // Lancamento de R$1000, vencendo em 10/01, pago com 10 dias de atraso (20/01)
        // Multa padrao: 2% = R$20,00
        // Juros padrao: 0,033%/dia * 10 dias = 0,33% = R$3,30
        // Total esperado: 1000 + 20 + 3.30 = 1023.30
        ContaAReceber lancamento = new ContaAReceber(
                cliente, "Servico prestado", new BigDecimal("1000.00"), LocalDate.of(2026, 1, 10)
        );

        BigDecimal valorAtualizado = lancamento.calcularValorAtualizado(LocalDate.of(2026, 1, 20));

        assertThat(valorAtualizado).isEqualByComparingTo("1023.30");
    }

    @Test
    void deveIdentificarTipoCorretoParaContaAPagar() {
        ContaAPagar lancamento = new ContaAPagar(
                cliente, "Fornecedor XPTO", new BigDecimal("500.00"), LocalDate.of(2026, 2, 1)
        );

        assertThat(lancamento.getTipo()).isEqualTo(TipoLancamento.PAGAR);
    }

    @Test
    void deveIdentificarTipoCorretoParaContaAReceber() {
        ContaAReceber lancamento = new ContaAReceber(
                cliente, "Cliente XPTO", new BigDecimal("500.00"), LocalDate.of(2026, 2, 1)
        );

        assertThat(lancamento.getTipo()).isEqualTo(TipoLancamento.RECEBER);
    }

    @Test
    void deveMarcarComoPagoCorretamente() {
        ContaAReceber lancamento = new ContaAReceber(
                cliente, "Servico prestado", new BigDecimal("1000.00"), LocalDate.of(2026, 1, 10)
        );

        lancamento.marcarComoPago(LocalDate.of(2026, 1, 12));

        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PAGO);
        assertThat(lancamento.getDataPagamento()).isEqualTo(LocalDate.of(2026, 1, 12));
    }
}