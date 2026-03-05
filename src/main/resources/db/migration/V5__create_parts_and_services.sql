-- Tabela para os Serviços realizados na OS
CREATE TABLE provided_services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);

-- Tabela para as Peças usadas na OS
CREATE TABLE used_parts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);