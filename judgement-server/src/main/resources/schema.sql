CREATE TABLE IF NOT EXISTS Judgement (
    judgement_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    approval_amount DECIMAL(15, 2) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    pay_day TINYINT NOT NULL,
    total DECIMAL(15, 2) NOT NULL,
    monthly_payment DECIMAL(15, 2) NOT NULL,
    number_of_payments INT NOT NULL,
    interest DECIMAL(4, 2) NOT NULL,
    created_at date NOT NULL,
    created_by varchar(20) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(20) DEFAULT NULL,
    is_deleted boolean DEFAULT false
);
