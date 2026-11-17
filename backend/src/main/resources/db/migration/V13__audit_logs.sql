-- F13 Audit log: who did what to which record. actor_id is NULL for scheduled jobs.
-- See docs/06-database-design.md#audit_logs and docs/features/F13-audit-log.md

CREATE TABLE audit_logs (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    actor_id    BIGINT      NULL,
    action      VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id   BIGINT      NULL,
    details     JSON        NULL,
    created_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_audit_logs_actor FOREIGN KEY (actor_id) REFERENCES users (id),
    INDEX idx_audit_logs_entity (entity_type, entity_id),
    INDEX idx_audit_logs_created_at (created_at),
    INDEX idx_audit_logs_actor (actor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
