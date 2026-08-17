package com.danilo.fluxocaixa.repository;

import com.danilo.fluxocaixa.domain.LancamentoFinanceiro;
import com.danilo.fluxocaixa.domain.StatusLancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, Long> {

    List<LancamentoFinanceiro> findByClienteId(Long clienteId);

    List<LancamentoFinanceiro> findByStatus(StatusLancamento status);

    List<LancamentoFinanceiro> findByStatusAndDataVencimentoBefore(StatusLancamento status, LocalDate data);

    List<LancamentoFinanceiro> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);
}