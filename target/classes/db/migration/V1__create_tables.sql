CREATE TABLE proposta (
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL,
    valor_solicitado NUMERIC(19, 2) NOT NULL,
    quantidade_parcelas INT NOT NULL,
    data_solicitacao DATE NOT NULL
);

CREATE TABLE parcela (
    id BIGSERIAL PRIMARY KEY,
    numero INT NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    proposta_id BIGINT NOT NULL REFERENCES proposta(id) ON DELETE CASCADE
);