-- F04 Inventory: physical copies of a book.
-- See docs/06-database-design.md#book_copies and docs/features/F04-inventory.md

CREATE TABLE book_copies (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    book_id     BIGINT      NOT NULL,
    barcode     VARCHAR(50) NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    location    VARCHAR(50) NULL,
    acquired_at DATE        NULL,
    created_at  DATETIME(6) NOT NULL,
    updated_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_book_copies_barcode UNIQUE (barcode),
    CONSTRAINT fk_book_copies_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT ck_book_copies_status
        CHECK (status IN ('AVAILABLE', 'ON_LOAN', 'ON_HOLD', 'LOST', 'DAMAGED', 'WITHDRAWN')),
    INDEX idx_book_copies_book_status (book_id, status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
