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

-- Index for efficient queries filtering by card_id
CREATE INDEX IF NOT EXISTS idx_card_transactions_card_id ON card_transactions(card_id);

-- Composite index for aggregation queries (card_id + type for SUM operations)
CREATE INDEX IF NOT EXISTS idx_card_transactions_card_type ON card_transactions(card_id, type);
