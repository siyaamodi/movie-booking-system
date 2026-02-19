package moviebookingsystem.repository;

import moviebookingsystem.entity.Movie;
import moviebookingsystem.enums.Genre;
import moviebookingsystem.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByLanguageAndGenreAndIsActiveTrue(Language language, Genre genre);
    List<Movie> findByIsActiveTrue();  // Fixed
    List<Movie> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title);
    List<Movie> findByGenreAndIsActiveTrue(Genre genre);
    List<Movie> findByLanguageAndIsActiveTrue(Language language);
    List<Movie> findByReleaseDateBetweenAndIsActiveTrue(LocalDate startDate, LocalDate endDate);
    List<Movie> findByReleaseDateAfterAndIsActiveTrue(LocalDate date);
}