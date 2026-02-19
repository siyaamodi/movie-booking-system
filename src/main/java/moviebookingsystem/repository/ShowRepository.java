package moviebookingsystem.repository;

import moviebookingsystem.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieIdAndIsActiveTrue(Long movieId);
    List<Show> findByTheaterIdAndIsActiveTrue(Long theaterId);
    List<Show> findByMovieIdAndTheaterIdAndIsActiveTrue(Long movieId, Long theaterId);
    List<Show> findByIsActiveTrue();  // Fixed
    List<Show> findByShowDateAndIsActiveTrue(LocalDate showDate);
}