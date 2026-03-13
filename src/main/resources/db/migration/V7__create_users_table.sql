CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Injeta o seu usuário de testes padrão (Senha: 123456)
INSERT INTO users (name, password, role) VALUES ('admin', '123456', 'ADMIN');