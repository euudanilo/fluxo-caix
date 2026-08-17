package com.danilo.fluxocaixa.service;

public class DocumentoJaCadastradoException extends RuntimeException {

    public DocumentoJaCadastradoException(String documento) {
        super("Ja existe um cliente cadastrado com o documento: " + documento);
    }
}