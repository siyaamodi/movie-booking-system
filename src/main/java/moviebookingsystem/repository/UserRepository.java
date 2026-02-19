package moviebookingsystem.repository;

import moviebookingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByIsActiveTrue();  // Fixed
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
    Optional<User> findByMobileNumber(String mobileNumber);
}