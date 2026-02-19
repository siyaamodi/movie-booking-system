package moviebookingsystem.repository;

import moviebookingsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByShowId(Long showId);
    List<Ticket> findByUserIdAndShowId(Long userId, Long showId);
    List<Ticket> findByIsActiveTrue();  // Fixed
    List<Ticket> findByUserIdAndIsActiveTrue(Long userId);
}