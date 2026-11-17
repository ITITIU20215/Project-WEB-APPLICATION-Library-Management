package com.library.fine.repository;

import com.library.fine.entity.Fine;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {

    Optional<Fine> findByLoanId(Long loanId);
}
