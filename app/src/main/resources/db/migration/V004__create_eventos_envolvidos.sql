CREATE TABLE IF NOT EXISTS calendario.eventos_envolvidos (
    evento_id UUID NOT NULL,
    organizacao_id UUID NOT NULL,
    papel_participacao VARCHAR(64),
    PRIMARY KEY (evento_id, organizacao_id)
);
