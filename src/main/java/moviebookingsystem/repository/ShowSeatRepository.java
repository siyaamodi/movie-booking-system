package moviebookingsystem.repository;

import moviebookingsystem.entity.ShowSeat;
import moviebookingsystem.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShowIdAndIsActiveTrue(Long showId);
    List<ShowSeat> findByShowIdAndStatus(Long showId, SeatStatus status);
    List<ShowSeat> findByIsActiveTrue();  // Fixed
}