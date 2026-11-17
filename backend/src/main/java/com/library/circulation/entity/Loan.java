package com.library.circulation.entity;

import com.library.common.entity.BaseEntity;
import com.library.inventory.entity.BookCopy;
import com.library.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Table {@code loans} (F06). The database allows at most one {@code ACTIVE} loan per copy (unique
 * generated column {@code active_copy_id}, not mapped here).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "copy_id", nullable = false)
    private BookCopy copy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    /** Staff member who checked the copy out. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issued_by", nullable = false)
    private User issuedBy;

    @Column(name = "checkout_at", nullable = false)
    private LocalDateTime checkoutAt;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    /** Staff member who took the copy back. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returned_to")
    private User returnedTo;

    @Column(name = "renew_count", nullable = false)
    private int renewCount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private LoanStatus status = LoanStatus.ACTIVE;

    public boolean isOverdue(LocalDate today) {
        return status == LoanStatus.ACTIVE && dueDate.isBefore(today);
    }
}
