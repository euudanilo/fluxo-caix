package com.danilo.fluxocaixa.controller;

import com.danilo.fluxocaixa.domain.Cliente;
import com.danilo.fluxocaixa.dto.ClienteResponse;
import com.danilo.fluxocaixa.dto.CriarClienteRequest;
import com.danilo.fluxocaixa.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody CriarClienteRequest request) {
        Cliente cliente = clienteService.cadastrar(
                request.nome(),
                request.documento(),
                request.email(),
                request.telefone()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponse.from(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteResponse.from(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(ClienteResponse::from)
                .toList();

        return ResponseEntity.ok(clientes);
    }
}