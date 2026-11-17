-- F06 Circulation: loans.
-- See docs/06-database-design.md#loans and docs/features/F06-circulation.md
-- "Overdue" is derived (status = 'ACTIVE' AND due_date < today), never stored.

CREATE TABLE loans (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    copy_id     BIGINT      NOT NULL,
    member_id   BIGINT      NOT NULL,
    issued_by   BIGINT      NOT NULL,
    checkout_at DATETIME(6) NOT NULL,
    due_date    DATE        NOT NULL,
    returned_at DATETIME(6) NULL,
    returned_to BIGINT      NULL,
    renew_count INT         NOT NULL DEFAULT 0,
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at  DATETIME(6) NOT NULL,
    updated_at  DATETIME(6) NOT NULL,
    -- Safety net for "at most one ACTIVE loan per copy". Not mapped in JPA.
    active_copy_id BIGINT AS (IF(status = 'ACTIVE', copy_id, NULL)) STORED,
    PRIMARY KEY (id),
    CONSTRAINT fk_loans_copy FOREIGN KEY (copy_id) REFERENCES book_copies (id),
    CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES users (id),
    CONSTRAINT fk_loans_issued_by FOREIGN KEY (issued_by) REFERENCES users (id),
    CONSTRAINT fk_loans_returned_to FOREIGN KEY (returned_to) REFERENCES users (id),
    CONSTRAINT uk_loans_active_copy UNIQUE (active_copy_id),
    CONSTRAINT ck_loans_status CHECK (status IN ('ACTIVE', 'RETURNED')),
    CONSTRAINT ck_loans_renew_count CHECK (renew_count >= 0),
    INDEX idx_loans_member_status (member_id, status),
    INDEX idx_loans_copy_status (copy_id, status),
    INDEX idx_loans_due_date (due_date),
    INDEX idx_loans_checkout_at (checkout_at),
    INDEX idx_loans_returned_at (returned_at),
    INDEX idx_loans_issued_by (issued_by),
    INDEX idx_loans_returned_to (returned_to)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
