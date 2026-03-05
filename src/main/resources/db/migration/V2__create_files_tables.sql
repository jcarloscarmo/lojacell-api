-- Tabela para Fotos do Aparelho (Pode ter várias por OS)
CREATE TABLE work_order_photos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    original_name VARCHAR(255),
    content_type VARCHAR(50),
    size_bytes BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_photo_os FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE
);

-- Tabela para Assinaturas (Entrada e Entrega)
CREATE TABLE work_order_signatures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    type ENUM('ENTRY', 'DELIVERY') NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    content_type VARCHAR(50),
    size_bytes BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_signature_os FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE
);