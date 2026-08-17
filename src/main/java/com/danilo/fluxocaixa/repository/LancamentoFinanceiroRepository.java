package com.danilo.fluxocaixa.repository;

import com.danilo.fluxocaixa.domain.LancamentoFinanceiro;
import com.danilo.fluxocaixa.domain.StatusLancamento;
import com.danilo.fluxocaixa.domain.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, Long> {

    List<LancamentoFinanceiro> findByClienteId(Long clienteId);

    List<LancamentoFinanceiro> findByStatus(StatusLancamento status);

    List<LancamentoFinanceiro> findByStatusAndDataVencimentoBefore(StatusLancamento status, LocalDate data);

    List<LancamentoFinanceiro> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);

    @Query("""
            SELECT COALESCE(SUM(l.valorOriginal), 0)
            FROM LancamentoFinanceiro l
            WHERE TYPE(l) = :tipoClasse
            AND l.status <> com.danilo.fluxocaixa.domain.StatusLancamento.PAGO
            AND l.dataVencimento BETWEEN :inicio AND :fim
            """)
    BigDecimal somarValorPorTipoEPeriodo(@Param("tipoClasse") Class<? extends LancamentoFinanceiro> tipoClasse,
                                         @Param("inicio") LocalDate inicio,
                                         @Param("fim") LocalDate fim);
}