package com.danilo.fluxocaixa.exception;

import com.danilo.fluxocaixa.service.ClienteNaoEncontradoException;
import com.danilo.fluxocaixa.service.DocumentoJaCadastradoException;
import com.danilo.fluxocaixa.service.LancamentoNaoEncontradoException;
import com.danilo.fluxocaixa.service.PeriodoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(404, "Cliente nao encontrado", ex.getMessage()));
    }

    @ExceptionHandler(LancamentoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleLancamentoNaoEncontrado(LancamentoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(404, "Lancamento nao encontrado", ex.getMessage()));
    }

    @ExceptionHandler(DocumentoJaCadastradoException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoDuplicado(DocumentoJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(409, "Documento ja cadastrado", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(400, "Dados invalidos", "Um ou mais campos estao invalidos", detalhes));
    }

    @ExceptionHandler(PeriodoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handlePeriodoInvalido(PeriodoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(400, "Periodo invalido", ex.getMessage()));
    }
}