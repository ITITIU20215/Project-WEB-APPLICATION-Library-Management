-- F09 Loan rules & system settings: key/value store + default values.
-- See docs/06-database-design.md#settings and docs/features/F09-loan-rules-settings.md
-- `key` and `value` are reserved words in MySQL, hence setting_key / setting_value.

CREATE TABLE settings (
    setting_key   VARCHAR(100) NOT NULL,
    setting_value VARCHAR(255) NOT NULL,
    value_type    VARCHAR(20)  NOT NULL,
    description   VARCHAR(255) NULL,
    updated_by    BIGINT       NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (setting_key),
    CONSTRAINT fk_settings_updated_by FOREIGN KEY (updated_by) REFERENCES users (id),
    CONSTRAINT ck_settings_value_type CHECK (value_type IN ('INT', 'DECIMAL', 'BOOLEAN', 'STRING'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO settings (setting_key, setting_value, value_type, description, updated_at) VALUES
    ('loan.period.days',     '14',    'INT',     'Number of days a loan lasts',                          UTC_TIMESTAMP(6)),
    ('loan.max.active',      '5',     'INT',     'Maximum active loans per member',                      UTC_TIMESTAMP(6)),
    ('loan.max.renewals',    '2',     'INT',     'Maximum renewals per loan',                            UTC_TIMESTAMP(6)),
    ('fine.per.day',         '5000',  'DECIMAL', 'Fine per overdue day (VND)',                           UTC_TIMESTAMP(6)),
    ('fine.block.threshold', '50000', 'DECIMAL', 'Outstanding fines (VND) at which borrowing is blocked', UTC_TIMESTAMP(6)),
    ('hold.pickup.days',     '3',     'INT',     'Days a READY hold waits for pickup',                   UTC_TIMESTAMP(6));
