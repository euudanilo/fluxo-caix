package com.danilo.fluxocaixa.dto;

import com.danilo.fluxocaixa.domain.Cliente;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String documento,
        String email,
        String telefone,
        LocalDateTime criadoEm
) {
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getDocumento(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCriadoEm()
        );
    }
}