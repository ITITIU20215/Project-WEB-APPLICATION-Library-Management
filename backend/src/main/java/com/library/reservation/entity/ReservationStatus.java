package com.library.reservation.entity;

/** PENDING and READY are "open" holds; the others are final. */
public enum ReservationStatus {
    PENDING,
    READY,
    FULFILLED,
    CANCELLED,
    EXPIRED
}
