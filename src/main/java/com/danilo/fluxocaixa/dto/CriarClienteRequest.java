package com.danilo.fluxocaixa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarClienteRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Size(max = 20) String documento,
        @Email @Size(max = 150) String email,
        @Size(max = 20) String telefone
) {
}