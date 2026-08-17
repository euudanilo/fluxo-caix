CREATE TABLE cliente (
                         id BIGSERIAL PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         documento VARCHAR(20) NOT NULL UNIQUE,
                         email VARCHAR(150),
                         telefone VARCHAR(20),
                         criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_cliente_documento ON cliente (documento);