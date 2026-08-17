CREATE TABLE lancamento_financeiro (
                                       id BIGSERIAL PRIMARY KEY,
                                       cliente_id BIGINT NOT NULL REFERENCES cliente(id),
                                       tipo VARCHAR(10) NOT NULL,
                                       descricao VARCHAR(200) NOT NULL,
                                       valor_original NUMERIC(15,2) NOT NULL,
                                       data_vencimento DATE NOT NULL,
                                       data_pagamento DATE,
                                       status VARCHAR(15) NOT NULL DEFAULT 'PENDENTE',
                                       percentual_multa NUMERIC(5,2) NOT NULL DEFAULT 2.00,
                                       percentual_juros_dia NUMERIC(5,2) NOT NULL DEFAULT 0.033,
                                       criado_em TIMESTAMP NOT NULL DEFAULT now(),

                                       CONSTRAINT chk_tipo CHECK (tipo IN ('PAGAR', 'RECEBER')),
                                       CONSTRAINT chk_status CHECK (status IN ('PENDENTE', 'VENCIDO', 'PAGO'))
);

CREATE INDEX idx_lancamento_cliente ON lancamento_financeiro (cliente_id);
CREATE INDEX idx_lancamento_status ON lancamento_financeiro (status);
CREATE INDEX idx_lancamento_vencimento ON lancamento_financeiro (data_vencimento);