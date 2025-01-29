CREATE TABLE IF NOT EXISTS application (
    application_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    cell_phone VARCHAR(12) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    interest_rate DECIMAL(5,4) DEFAULT NULL,
    fee DECIMAL(5,4) DEFAULT NULL,
    maturity DATETIME DEFAULT NULL,
    hope_amount DECIMAL(15,2) NOT NULL,
    applied_at DATETIME NOT NULL,
    contracted_at DATETIME DEFAULT NULL,
    approval_amount DECIMAL(15,2) DEFAULT NULL,
    communication_status VARCHAR(50) DEFAULT NULL,
    created_at DATE NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at DATE DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);
