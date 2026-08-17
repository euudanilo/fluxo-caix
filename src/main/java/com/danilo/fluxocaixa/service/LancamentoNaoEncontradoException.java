package com.danilo.fluxocaixa.service;

public class LancamentoNaoEncontradoException extends RuntimeException {

    public LancamentoNaoEncontradoException(Long lancamentoId) {
        super("Lancamento nao encontrado com id: " + lancamentoId);
    }
}