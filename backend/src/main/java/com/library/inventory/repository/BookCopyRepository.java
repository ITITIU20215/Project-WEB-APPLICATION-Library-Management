package com.library.inventory.repository;

import com.library.inventory.entity.BookCopy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    Optional<BookCopy> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);
}
