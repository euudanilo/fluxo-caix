package com.danilo.fluxocaixa.service;

import java.time.LocalDate;

public class PeriodoInvalidoException extends RuntimeException {

    public PeriodoInvalidoException(LocalDate dataInicio, LocalDate dataFim) {
        super("Data fim (" + dataFim + ") nao pode ser anterior a data inicio (" + dataInicio + ")");
    }
}