-- F08 Fines: one fine per overdue loan, paid in one or more payments. Amounts in VND.
-- See docs/06-database-design.md#fines and docs/features/F08-fines.md

CREATE TABLE fines (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    loan_id      BIGINT        NOT NULL,
    member_id    BIGINT        NOT NULL,
    days_overdue INT           NOT NULL,
    amount       DECIMAL(12,2) NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'UNPAID',
    waived_by    BIGINT        NULL,
    waive_reason VARCHAR(255)  NULL,
    created_at   DATETIME(6)   NOT NULL,
    updated_at   DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_fines_loan UNIQUE (loan_id),
    CONSTRAINT fk_fines_loan FOREIGN KEY (loan_id) REFERENCES loans (id),
    CONSTRAINT fk_fines_member FOREIGN KEY (member_id) REFERENCES users (id),
    CONSTRAINT fk_fines_waived_by FOREIGN KEY (waived_by) REFERENCES users (id),
    CONSTRAINT ck_fines_status CHECK (status IN ('UNPAID', 'PAID', 'WAIVED')),
    CONSTRAINT ck_fines_amount CHECK (amount >= 0),
    CONSTRAINT ck_fines_days CHECK (days_overdue > 0),
    INDEX idx_fines_member_status (member_id, status),
    INDEX idx_fines_waived_by (waived_by)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE fine_payments (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    fine_id     BIGINT        NOT NULL,
    amount      DECIMAL(12,2) NOT NULL,
    method      VARCHAR(20)   NOT NULL,
    received_by BIGINT        NOT NULL,
    paid_at     DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_fine_payments_fine FOREIGN KEY (fine_id) REFERENCES fines (id),
    CONSTRAINT fk_fine_payments_received_by FOREIGN KEY (received_by) REFERENCES users (id),
    CONSTRAINT ck_fine_payments_amount CHECK (amount > 0),
    CONSTRAINT ck_fine_payments_method CHECK (method IN ('CASH', 'TRANSFER')),
    INDEX idx_fine_payments_fine (fine_id),
    INDEX idx_fine_payments_received_by (received_by),
    INDEX idx_fine_payments_paid_at (paid_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
