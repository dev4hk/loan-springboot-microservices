CREATE TABLE IF NOT EXISTS Judgement (
    judgementId BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    applicationId BIGINT NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    approvalAmount DECIMAL(15, 2) NOT NULL,
    created_at date NOT NULL,
    created_by varchar(20) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(20) DEFAULT NULL,
    is_deleted boolean DEFAULT false
);
