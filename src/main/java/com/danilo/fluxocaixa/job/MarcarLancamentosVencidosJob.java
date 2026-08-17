package com.danilo.fluxocaixa.job;

import com.danilo.fluxocaixa.domain.LancamentoFinanceiro;
import com.danilo.fluxocaixa.domain.StatusLancamento;
import com.danilo.fluxocaixa.repository.LancamentoFinanceiroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class MarcarLancamentosVencidosJob {

    private static final Logger log = LoggerFactory.getLogger(MarcarLancamentosVencidosJob.class);

    private final LancamentoFinanceiroRepository lancamentoRepository;

    public MarcarLancamentosVencidosJob(LancamentoFinanceiroRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void executar() {
        List<LancamentoFinanceiro> pendentesVencidos = lancamentoRepository
                .findByStatusAndDataVencimentoBefore(StatusLancamento.PENDENTE, LocalDate.now());

        if (pendentesVencidos.isEmpty()) {
            log.info("Nenhum lancamento pendente vencido encontrado.");
            return;
        }

        pendentesVencidos.forEach(LancamentoFinanceiro::marcarComoVencido);
        lancamentoRepository.saveAll(pendentesVencidos);

        log.info("{} lancamento(s) marcado(s) como VENCIDO.", pendentesVencidos.size());
    }
}