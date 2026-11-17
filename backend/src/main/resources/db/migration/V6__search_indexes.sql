-- F05 Search: indexes for search and filters.
-- See docs/features/F05-search.md (idx_authors_name was already created in V3).

CREATE FULLTEXT INDEX ft_books_title ON books (title, subtitle);
CREATE INDEX idx_books_language ON books (language);
CREATE INDEX idx_books_year ON books (publication_year);
