package com.library.circulation.entity;

/** "Overdue" is not a status: it is derived with {@link Loan#isOverdue(java.time.LocalDate)}. */
public enum LoanStatus {
    ACTIVE,
    RETURNED
}
