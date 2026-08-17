package com.danilo.fluxocaixa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "lancamento_financeiro")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class LancamentoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotBlank
    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    @NotNull
    @Positive
    @Column(name = "valor_original", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorOriginal;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 15)
    private StatusLancamento status = StatusLancamento.PENDENTE;

    @Column(name = "percentual_multa", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualMulta = new BigDecimal("2.00");

    @Column(name = "percentual_juros_dia", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualJurosDia = new BigDecimal("0.033");

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }

    protected LancamentoFinanceiro() {
    }

    protected LancamentoFinanceiro(Cliente cliente, String descricao, BigDecimal valorOriginal, LocalDate dataVencimento) {
        this.cliente = cliente;
        this.descricao = descricao;
        this.valorOriginal = valorOriginal;
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal calcularValorAtualizado(LocalDate dataReferencia) {
        if (!dataReferencia.isAfter(dataVencimento)) {
            return valorOriginal;
        }

        long diasAtraso = ChronoUnit.DAYS.between(dataVencimento, dataReferencia);

        BigDecimal multa = valorOriginal
                .multiply(percentualMulta)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal juros = valorOriginal
                .multiply(percentualJurosDia)
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(diasAtraso));

        return valorOriginal
                .add(multa)
                .add(juros)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void marcarComoPago(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
        this.status = StatusLancamento.PAGO;
    }

    public void marcarComoVencido() {
        if (this.status == StatusLancamento.PENDENTE) {
            this.status = StatusLancamento.VENCIDO;
        }
    }

    public abstract TipoLancamento getTipo();

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public StatusLancamento getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}