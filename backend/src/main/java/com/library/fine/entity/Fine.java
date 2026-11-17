package com.library.fine.entity;

import com.library.circulation.entity.Loan;
import com.library.common.entity.BaseEntity;
import com.library.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Table {@code fines} (F08). At most one fine per loan. Outstanding = {@code amount} minus the sum
 * of {@link #getPayments() payments}. Amounts are in VND.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "fines")
public class Fine extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /** Denormalised from the loan for quick per-member sums. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name = "days_overdue", nullable = false)
    private int daysOverdue;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private FineStatus status = FineStatus.UNPAID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waived_by")
    private User waivedBy;

    @Column(name = "waive_reason", length = 255)
    private String waiveReason;

    @OneToMany(mappedBy = "fine")
    @OrderBy("paidAt ASC")
    private List<FinePayment> payments = new ArrayList<>();
}
