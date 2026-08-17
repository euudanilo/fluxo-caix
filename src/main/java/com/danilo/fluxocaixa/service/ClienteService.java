package com.danilo.fluxocaixa.service;

import com.danilo.fluxocaixa.domain.Cliente;
import com.danilo.fluxocaixa.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(String nome, String documento, String email, String telefone) {
        if (clienteRepository.existsByDocumento(documento)) {
            throw new DocumentoJaCadastradoException(documento);
        }

        Cliente cliente = new Cliente(nome, documento, email, telefone);
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
}