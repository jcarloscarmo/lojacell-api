-- Cria a tabela do Caixa Diário
CREATE TABLE cash_registers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    opening_date DATETIME NOT NULL,
    closing_date DATETIME,
    opening_balance DECIMAL(10,2) NOT NULL,
    closing_balance DECIMAL(10,2),
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_cash_register_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Cria a tabela de Movimentações (Entradas/Saídas)
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cash_register_id BIGINT NOT NULL,
    work_order_id BIGINT, -- Pode ser nulo se for venda avulsa ou despesa
    type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    transaction_date DATETIME NOT NULL,
    CONSTRAINT fk_transaction_cash_register FOREIGN KEY (cash_register_id) REFERENCES cash_registers(id),
    CONSTRAINT fk_transaction_work_order FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);