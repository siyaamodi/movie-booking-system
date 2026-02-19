package moviebookingsystem.repository;

import moviebookingsystem.entity.Payment;
import moviebookingsystem.enums.PaymentMethod;
import moviebookingsystem.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByTicketId(Long ticketId);
    Optional<Payment> findByBookingId(Long bookingId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Payment> findByTicketUserId(Long userId);
    List<Payment> findByTicketUserIdAndStatus(Long userId, PaymentStatus status);
    List<Payment> findByIsActiveTrue();  // Fixed
    boolean existsByTransactionId(String transactionId);
}