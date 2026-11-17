package com.library.review.repository;

import com.library.review.entity.WishlistItem;
import com.library.review.entity.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {}
