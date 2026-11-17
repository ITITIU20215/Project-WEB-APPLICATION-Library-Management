-- Demo data for development: staff/member accounts and copies of the V4 sample books.
-- Password for every account below: Password@123
-- Feature migrations added after this file start at V15.

INSERT INTO users (email, password_hash, full_name, phone, role, status, must_change_password, created_at, updated_at) VALUES
    ('librarian@library.local', '$2a$10$/E0AF9hECPWA6QYDxqBDEeKq4kS9RCgyja/ggSC6BeprwIQ5veaGq',
        'Lê Thủ Thư',     '0901000001', 'LIBRARIAN', 'ACTIVE',      FALSE, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('member1@library.local',   '$2a$10$/E0AF9hECPWA6QYDxqBDEeKq4kS9RCgyja/ggSC6BeprwIQ5veaGq',
        'Nguyễn Văn An',  '0901000002', 'MEMBER',    'ACTIVE',      FALSE, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('member2@library.local',   '$2a$10$/E0AF9hECPWA6QYDxqBDEeKq4kS9RCgyja/ggSC6BeprwIQ5veaGq',
        'Trần Thị Bình',  '0901000003', 'MEMBER',    'ACTIVE',      FALSE, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('member3@library.local',   '$2a$10$/E0AF9hECPWA6QYDxqBDEeKq4kS9RCgyja/ggSC6BeprwIQ5veaGq',
        'Phạm Văn Cường', NULL,         'MEMBER',    'DEACTIVATED', FALSE, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));

-- Three AVAILABLE copies per sample book. Barcode: LIB-<book id>-<n>, location: <shelf>-<book id>-<n>.
INSERT INTO book_copies (book_id, barcode, status, location, acquired_at, created_at, updated_at)
SELECT b.id,
       CONCAT('LIB-', LPAD(b.id, 4, '0'), '-', n.n),
       'AVAILABLE',
       CONCAT(CHAR(65 + (b.id - 1) DIV 5), '-', LPAD(b.id, 2, '0'), '-', n.n),
       DATE '2025-01-15',
       UTC_TIMESTAMP(6),
       UTC_TIMESTAMP(6)
FROM books b
CROSS JOIN (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3) n
WHERE b.id BETWEEN 1 AND 20
ORDER BY b.id, n.n;
