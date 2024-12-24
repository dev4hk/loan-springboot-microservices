CREATE TABLE IF NOT EXISTS `counsel` (
    `counsel_id` int AUTO_INCREMENT  PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `address` varchar(100) NOT NULL,
    `address_detail` varchar(100) NOT NULL,
    `zip_code` varchar(10) NOT NULL,
    `cell_phone` varchar(10) NOT NULL,
    `email` varchar(100) NOT NULL,
    `memo` text NOT NULL,
    `applied_at` date NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL,
    `is_deleted` boolean DEFAULT false
);