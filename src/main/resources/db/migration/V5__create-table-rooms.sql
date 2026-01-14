CREATE TABLE rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    total_rows INT NOT NULL,
    total_columns INT NOT NULL,
    active BOOLEAN DEFAULT TRUE
);