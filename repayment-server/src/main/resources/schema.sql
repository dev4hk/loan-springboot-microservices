CREATE TABLE IF NOT EXISTS repayment (
    repayment_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    application_id BIGINT NOT NULL,
    repayment_amount DECIMAL(15,2) NOT NULL,
    created_at date NOT NULL,
    created_by varchar(20) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(20) DEFAULT NULL,
    is_deleted boolean DEFAULT false
);
