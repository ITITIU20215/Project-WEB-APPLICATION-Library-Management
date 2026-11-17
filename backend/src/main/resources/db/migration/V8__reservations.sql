-- F07 Reservations: the hold queue (per book, ordered by created_at).
-- See docs/06-database-design.md#reservations and docs/features/F07-reservations.md

CREATE TABLE reservations (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    book_id    BIGINT      NOT NULL,
    member_id  BIGINT      NOT NULL,
    copy_id    BIGINT      NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    ready_at   DATETIME(6) NULL,
    expires_at DATE        NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    -- Safety net for BR-2: at most one open (PENDING/READY) hold per member and book. Not mapped in JPA.
    open_hold_key VARCHAR(41) AS (IF(status IN ('PENDING', 'READY'), CONCAT(member_id, ':', book_id), NULL)) STORED,
    PRIMARY KEY (id),
    CONSTRAINT fk_reservations_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_reservations_member FOREIGN KEY (member_id) REFERENCES users (id),
    CONSTRAINT fk_reservations_copy FOREIGN KEY (copy_id) REFERENCES book_copies (id),
    CONSTRAINT uk_reservations_open_hold UNIQUE (open_hold_key),
    CONSTRAINT ck_reservations_status
        CHECK (status IN ('PENDING', 'READY', 'FULFILLED', 'CANCELLED', 'EXPIRED')),
    INDEX idx_reservations_queue (book_id, status, created_at),
    INDEX idx_reservations_member_status (member_id, status),
    INDEX idx_reservations_copy (copy_id),
    INDEX idx_reservations_expires (status, expires_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
