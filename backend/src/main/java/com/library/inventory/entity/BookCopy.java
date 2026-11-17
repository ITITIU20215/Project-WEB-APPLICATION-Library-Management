package com.library.inventory.entity;

import com.library.catalog.entity.Book;
import com.library.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** Table {@code book_copies} (F04). Barcode is unique and stored upper-case. */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book_copies")
public class BookCopy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, length = 50)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private CopyStatus status = CopyStatus.AVAILABLE;

    @Column(length = 50)
    private String location;

    @Column(name = "acquired_at")
    private LocalDate acquiredAt;
}
