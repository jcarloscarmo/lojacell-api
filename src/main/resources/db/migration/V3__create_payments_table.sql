CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    method VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    notes TEXT,
    paid_at DATETIME NOT NULL,
    CONSTRAINT fk_payment_work_order FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);