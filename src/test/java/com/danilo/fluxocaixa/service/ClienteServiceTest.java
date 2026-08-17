package com.danilo.fluxocaixa.service;

import com.danilo.fluxocaixa.domain.Cliente;
import com.danilo.fluxocaixa.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void deveCadastrarClienteQuandoDocumentoNaoExiste() {
        when(clienteRepository.existsByDocumento("12345678900")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente cliente = clienteService.cadastrar(
                "Joao Silva", "12345678900", "joao@email.com", "31999999999"
        );

        assertThat(cliente.getNome()).isEqualTo("Joao Silva");
        assertThat(cliente.getDocumento()).isEqualTo("12345678900");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoDocumentoJaCadastrado() {
        when(clienteRepository.existsByDocumento("12345678900")).thenReturn(true);

        assertThatThrownBy(() ->
                clienteService.cadastrar("Joao Silva", "12345678900", "joao@email.com", "31999999999")
        )
                .isInstanceOf(DocumentoJaCadastradoException.class)
                .hasMessageContaining("12345678900");

        verify(clienteRepository, never()).save(any());
    }
}