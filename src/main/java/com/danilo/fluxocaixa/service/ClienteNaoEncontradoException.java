package com.danilo.fluxocaixa.service;

public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(Long clienteId) {
        super("Cliente nao encontrado com id: " + clienteId);
    }
}