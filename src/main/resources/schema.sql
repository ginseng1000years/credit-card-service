CREATE TABLE IF NOT EXISTS credit_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    credit_limit DECIMAL(19, 2) NOT NULL,
    available_limit DECIMAL(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS card_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (card_id) REFERENCES credit_cards(id)
);
