package com.danilo.fluxocaixa.service;

import com.danilo.fluxocaixa.domain.*;
import com.danilo.fluxocaixa.repository.ClienteRepository;
import com.danilo.fluxocaixa.repository.LancamentoFinanceiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class LancamentoFinanceiroService {

    private final LancamentoFinanceiroRepository lancamentoRepository;
    private final ClienteRepository clienteRepository;

    public LancamentoFinanceiroService(LancamentoFinanceiroRepository lancamentoRepository,
                                       ClienteRepository clienteRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public LancamentoFinanceiro cadastrar(Long clienteId, TipoLancamento tipo, String descricao,
                                          BigDecimal valorOriginal, LocalDate dataVencimento) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));

        LancamentoFinanceiro lancamento = switch (tipo) {
            case PAGAR -> new ContaAPagar(cliente, descricao, valorOriginal, dataVencimento);
            case RECEBER -> new ContaAReceber(cliente, descricao, valorOriginal, dataVencimento);
        };

        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public LancamentoFinanceiro registrarPagamento(Long lancamentoId, LocalDate dataPagamento) {
        LancamentoFinanceiro lancamento = lancamentoRepository.findById(lancamentoId)
                .orElseThrow(() -> new LancamentoNaoEncontradoException(lancamentoId));

        lancamento.marcarComoPago(dataPagamento);
        return lancamentoRepository.save(lancamento);
    }
}