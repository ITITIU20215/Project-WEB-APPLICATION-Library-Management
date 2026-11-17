-- F11 Notifications: in-app messages for members.
-- See docs/06-database-design.md#notifications and docs/features/F11-notifications.md
-- loan_id / reservation_id link a notification to what caused it, so the daily job
-- can check "already notified for this loan and type" before inserting.

CREATE TABLE notifications (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    user_id        BIGINT       NOT NULL,
    type           VARCHAR(30)  NOT NULL,
    message        VARCHAR(500) NOT NULL,
    loan_id        BIGINT       NULL,
    reservation_id BIGINT       NULL,
    read_at        DATETIME(6)  NULL,
    created_at     DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_notifications_loan FOREIGN KEY (loan_id) REFERENCES loans (id),
    CONSTRAINT fk_notifications_reservation FOREIGN KEY (reservation_id) REFERENCES reservations (id),
    CONSTRAINT ck_notifications_type CHECK (type IN ('DUE_SOON', 'OVERDUE', 'HOLD_READY')),
    INDEX idx_notifications_user_read (user_id, read_at),
    INDEX idx_notifications_loan_type (loan_id, type),
    INDEX idx_notifications_reservation (reservation_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
