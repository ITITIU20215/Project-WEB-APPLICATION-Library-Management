package com.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.library.audit.entity.AuditLog;
import com.library.audit.repository.AuditLogRepository;
import com.library.catalog.entity.Book;
import com.library.catalog.repository.BookRepository;
import com.library.circulation.entity.Loan;
import com.library.circulation.entity.LoanStatus;
import com.library.circulation.repository.LoanRepository;
import com.library.config.ClockConfig;
import com.library.config.JpaAuditingConfig;
import com.library.fine.entity.Fine;
import com.library.fine.entity.FinePayment;
import com.library.fine.entity.PaymentMethod;
import com.library.fine.repository.FinePaymentRepository;
import com.library.fine.repository.FineRepository;
import com.library.inventory.entity.BookCopy;
import com.library.inventory.entity.CopyStatus;
import com.library.inventory.repository.BookCopyRepository;
import com.library.notification.entity.Notification;
import com.library.notification.entity.NotificationType;
import com.library.notification.repository.NotificationRepository;
import com.library.reservation.entity.Reservation;
import com.library.reservation.entity.ReservationStatus;
import com.library.reservation.repository.ReservationRepository;
import com.library.review.entity.Review;
import com.library.review.entity.WishlistItem;
import com.library.review.repository.ReviewRepository;
import com.library.review.repository.WishlistItemRepository;
import com.library.setting.entity.SettingType;
import com.library.setting.repository.SettingRepository;
import com.library.user.entity.Role;
import com.library.user.entity.User;
import com.library.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

/**
 * Runs every Flyway migration against a real MySQL 8.4, lets Hibernate validate all entities
 * against the schema ({@code ddl-auto: validate}), and checks the key constraints. Skipped when
 * Docker is not available.
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ClockConfig.class, JpaAuditingConfig.class})
@Testcontainers(disabledWithoutDocker = true)
class DatabaseSchemaTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired EntityManager em;
    @Autowired UserRepository users;
    @Autowired BookRepository books;
    @Autowired BookCopyRepository copies;
    @Autowired LoanRepository loans;
    @Autowired ReservationRepository reservations;
    @Autowired FineRepository fines;
    @Autowired FinePaymentRepository finePayments;
    @Autowired SettingRepository settings;
    @Autowired NotificationRepository notifications;
    @Autowired ReviewRepository reviews;
    @Autowired WishlistItemRepository wishlist;
    @Autowired AuditLogRepository auditLogs;

    @Test
    void seedDataIsLoaded() {
        User admin = users.findByEmail("admin@library.local").orElseThrow();
        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(admin.isMustChangePassword()).isTrue();
        assertThat(users.count()).isEqualTo(5);

        assertThat(books.count()).isEqualTo(20);
        Book designPatterns = books.findByIsbn("9780201633610").orElseThrow();
        assertThat(designPatterns.getAuthors()).hasSize(4);
        assertThat(designPatterns.getCategories()).hasSize(2);
        assertThat(designPatterns.getPublisher().getName()).isEqualTo("Addison-Wesley");

        assertThat(copies.count()).isEqualTo(60);
        assertThat(copies.findByBarcode("LIB-0001-1").orElseThrow().getStatus())
                .isEqualTo(CopyStatus.AVAILABLE);

        assertThat(settings.count()).isEqualTo(6);
        var period = settings.findById("loan.period.days").orElseThrow();
        assertThat(period.getSettingValue()).isEqualTo("14");
        assertThat(period.getValueType()).isEqualTo(SettingType.INT);
    }

    @Test
    void everyEntityCanBeSavedAndReadBack() {
        User member = users.findByEmail("member1@library.local").orElseThrow();
        User librarian = users.findByEmail("librarian@library.local").orElseThrow();
        BookCopy copy = copies.findByBarcode("LIB-0001-1").orElseThrow();
        Book book = copy.getBook();

        Loan loan = loans.save(newLoan(copy, member, librarian));
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDateTime.now());
        loan.setReturnedTo(librarian);

        Fine fine = new Fine();
        fine.setLoan(loan);
        fine.setMember(member);
        fine.setDaysOverdue(3);
        fine.setAmount(new BigDecimal("15000.00"));
        fines.save(fine);

        FinePayment payment = new FinePayment();
        payment.setFine(fine);
        payment.setAmount(new BigDecimal("5000.00"));
        payment.setMethod(PaymentMethod.CASH);
        payment.setReceivedBy(librarian);
        finePayments.save(payment);

        Reservation hold = new Reservation();
        hold.setBook(books.findByIsbn("9780134685991").orElseThrow());
        hold.setMember(member);
        reservations.save(hold);

        Notification note = new Notification();
        note.setUser(member);
        note.setType(NotificationType.OVERDUE);
        note.setMessage("Clean Code is overdue");
        note.setLoan(loan);
        notifications.save(note);

        Review review = new Review();
        review.setBook(book);
        review.setMember(member);
        review.setRating(5);
        review.setComment("Great");
        reviews.save(review);

        wishlist.save(new WishlistItem(member, book));

        AuditLog log = new AuditLog();
        log.setActor(librarian);
        log.setAction("LOAN_CHECKOUT");
        log.setEntityType("LOAN");
        log.setEntityId(loan.getId());
        log.setDetails("{\"copyId\": " + copy.getId() + "}");
        auditLogs.save(log);

        em.flush();
        em.clear();

        Fine reloaded = fines.findByLoanId(loan.getId()).orElseThrow();
        assertThat(reloaded.getPayments()).hasSize(1);
        assertThat(reloaded.getCreatedAt()).isNotNull();
        assertThat(reloaded.getLoan().getReturnedTo().getId()).isEqualTo(librarian.getId());
        assertThat(finePayments.findAll()).allSatisfy(p -> assertThat(p.getPaidAt()).isNotNull());
        assertThat(reservations.findById(hold.getId()).orElseThrow().getStatus())
                .isEqualTo(ReservationStatus.PENDING);
        assertThat(notifications.findById(note.getId()).orElseThrow().getCreatedAt()).isNotNull();
        assertThat(reviews.findById(review.getId()).orElseThrow().getRating()).isEqualTo(5);
        assertThat(wishlist.count()).isEqualTo(1);
        assertThat(auditLogs.findById(log.getId()).orElseThrow().getDetails()).contains("copyId");
    }

    @Test
    void secondActiveLoanForSameCopyIsRejected() {
        User member = users.findByEmail("member1@library.local").orElseThrow();
        User other = users.findByEmail("member2@library.local").orElseThrow();
        User librarian = users.findByEmail("librarian@library.local").orElseThrow();
        BookCopy copy = copies.findByBarcode("LIB-0002-1").orElseThrow();

        loans.saveAndFlush(newLoan(copy, member, librarian));

        assertThatThrownBy(() -> loans.saveAndFlush(newLoan(copy, other, librarian)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void secondOpenHoldForSameBookIsRejected() {
        User member = users.findByEmail("member1@library.local").orElseThrow();
        Book book = books.findByIsbn("9780132350884").orElseThrow();

        reservations.saveAndFlush(newHold(book, member));

        assertThatThrownBy(() -> reservations.saveAndFlush(newHold(book, member)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void duplicateEmailIsRejected() {
        User copyOfAdmin = new User();
        copyOfAdmin.setEmail("admin@library.local");
        copyOfAdmin.setPasswordHash("x");
        copyOfAdmin.setFullName("Duplicate");
        copyOfAdmin.setRole(Role.MEMBER);

        assertThatThrownBy(() -> users.saveAndFlush(copyOfAdmin))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void ratingOutsideOneToFiveIsRejected() {
        Review review = new Review();
        review.setBook(books.findByIsbn("9780132350884").orElseThrow());
        review.setMember(users.findByEmail("member1@library.local").orElseThrow());
        review.setRating(6);

        assertThatThrownBy(() -> reviews.saveAndFlush(review)).isInstanceOf(DataAccessException.class);
    }

    private static Loan newLoan(BookCopy copy, User member, User librarian) {
        Loan loan = new Loan();
        loan.setCopy(copy);
        loan.setMember(member);
        loan.setIssuedBy(librarian);
        loan.setCheckoutAt(LocalDateTime.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        return loan;
    }

    private static Reservation newHold(Book book, User member) {
        Reservation hold = new Reservation();
        hold.setBook(book);
        hold.setMember(member);
        return hold;
    }
}
