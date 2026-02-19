package moviebookingsystem.repository;

import moviebookingsystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingReference(String bookingReference);
    List<Booking> findByIsActiveTrue();  // Fixed
    List<Booking> findByTicketId(Long ticketId);
    List<Booking> findByConfirmedTrue();
    List<Booking> findByConfirmedFalse();
    List<Booking> findByTicketUserId(Long userId);
    List<Booking> findByTicketUserIdAndConfirmedTrue(Long userId);
    List<Booking> findByTicketShowId(Long showId);
}