package com.library.fine.repository;

import com.library.fine.entity.FinePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinePaymentRepository extends JpaRepository<FinePayment, Long> {}
