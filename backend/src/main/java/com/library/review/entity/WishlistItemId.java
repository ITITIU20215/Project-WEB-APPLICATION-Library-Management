package com.library.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Composite primary key of {@code wishlist_items}: (member_id, book_id). */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class WishlistItemId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "book_id")
    private Long bookId;
}
