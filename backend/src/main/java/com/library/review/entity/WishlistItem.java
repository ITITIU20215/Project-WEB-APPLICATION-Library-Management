package com.library.review.entity;

import com.library.catalog.entity.Book;
import com.library.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** Table {@code wishlist_items} (F12). Unique per member and book (composite primary key). */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wishlist_items")
@EntityListeners(AuditingEntityListener.class)
public class WishlistItem {

    @EmbeddedId
    private WishlistItemId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private User member;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public WishlistItem(User member, Book book) {
        this.id = new WishlistItemId(member.getId(), book.getId());
        this.member = member;
        this.book = book;
    }
}
