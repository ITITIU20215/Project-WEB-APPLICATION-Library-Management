-- F01 Authentication: users table + seeded admin.
-- See docs/06-database-design.md#users and docs/features/F01-authentication.md

CREATE TABLE users (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    email                VARCHAR(255) NOT NULL,
    password_hash        VARCHAR(100) NOT NULL,
    full_name            VARCHAR(150) NOT NULL,
    phone                VARCHAR(20)  NULL,
    role                 VARCHAR(20)  NOT NULL,
    status               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    must_change_password BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at           DATETIME(6)  NOT NULL,
    updated_at           DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT ck_users_role CHECK (role IN ('MEMBER', 'LIBRARIAN', 'ADMIN')),
    CONSTRAINT ck_users_status CHECK (status IN ('ACTIVE', 'DEACTIVATED')),
    INDEX idx_users_role_status (role, status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- First admin. Password: Admin@123 (must be changed at first login).
INSERT INTO users (email, password_hash, full_name, role, status, must_change_password, created_at, updated_at)
VALUES ('admin@library.local',
        '$2a$10$fiMOt6O7DTy9Yd.G8q0Zm.ecDt7sEVrHeLWz6J8IgssC7a64fkuci',
        'System Administrator', 'ADMIN', 'ACTIVE', TRUE, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));
