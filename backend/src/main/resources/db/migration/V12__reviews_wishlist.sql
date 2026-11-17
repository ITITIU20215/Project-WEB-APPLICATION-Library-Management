-- F12 Reviews, ratings & wishlist.
-- See docs/06-database-design.md#reviews and docs/features/F12-reviews-wishlist.md

CREATE TABLE reviews (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    book_id    BIGINT      NOT NULL,
    member_id  BIGINT      NOT NULL,
    rating     TINYINT     NOT NULL,
    comment    TEXT        NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_reviews_book_member UNIQUE (book_id, member_id),
    CONSTRAINT fk_reviews_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_reviews_member FOREIGN KEY (member_id) REFERENCES users (id),
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),
    INDEX idx_reviews_member (member_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE wishlist_items (
    member_id  BIGINT      NOT NULL,
    book_id    BIGINT      NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (member_id, book_id),
    CONSTRAINT fk_wishlist_items_member FOREIGN KEY (member_id) REFERENCES users (id),
    CONSTRAINT fk_wishlist_items_book FOREIGN KEY (book_id) REFERENCES books (id),
    INDEX idx_wishlist_items_book (book_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
