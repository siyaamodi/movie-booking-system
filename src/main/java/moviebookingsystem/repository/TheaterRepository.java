package moviebookingsystem.repository;

import moviebookingsystem.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByCityAndIsActiveTrue(String city);
    List<Theater> findByIsActiveTrue();
}