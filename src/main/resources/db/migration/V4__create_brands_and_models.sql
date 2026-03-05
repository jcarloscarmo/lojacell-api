-- 1. Criação das Tabelas
CREATE TABLE brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE device_models (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand_id BIGINT NOT NULL,
    CONSTRAINT fk_model_brand FOREIGN KEY (brand_id) REFERENCES brands(id)
);

-- 2. Atualização da Tabela Devices (que será criada pela V1)
-- Como a V1 cria a tabela com as colunas antigas, a V4 as remove e coloca a nova
ALTER TABLE devices DROP COLUMN brand;
ALTER TABLE devices DROP COLUMN model;
ALTER TABLE devices ADD COLUMN model_id BIGINT NULL;

-- 3. Vínculo de Chave Estrangeira
ALTER TABLE devices ADD CONSTRAINT fk_device_model FOREIGN KEY (model_id) REFERENCES device_models(id);