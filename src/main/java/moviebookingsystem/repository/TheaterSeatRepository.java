package moviebookingsystem.repository;

import moviebookingsystem.entity.TheaterSeat;
import moviebookingsystem.enums.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

    // 1. Find all seats by theater ID
    List<TheaterSeat> findByTheaterId(Long theaterId);

    // 2. Find seats by theater ID and active status
    List<TheaterSeat> findByTheaterIdAndIsActive(Long theaterId, Boolean isActive);

    // 3. Find active seats by theater ID (active = true)
    List<TheaterSeat> findByTheaterIdAndIsActiveTrue(Long theaterId);

    // 4. Find inactive seats by theater ID (active = false)
    List<TheaterSeat> findByTheaterIdAndIsActiveFalse(Long theaterId);

    // 5. Check if a seat exists in a theater by row and number
    @Query("SELECT CASE WHEN COUNT(ts) > 0 THEN TRUE ELSE FALSE END " +
            "FROM TheaterSeat ts " +
            "WHERE ts.theater.id = :theaterId " +
            "AND ts.seatRow = :seatRow " +
            "AND ts.seatNumber = :seatNumber")
    boolean existsByTheaterIdAndSeatRowAndSeatNumber(
            @Param("theaterId") Long theaterId,
            @Param("seatRow") String seatRow,
            @Param("seatNumber") String seatNumber);

    // 6. Check if a seat exists in a theater (excluding current seat) - for update operations
    @Query("SELECT CASE WHEN COUNT(ts) > 0 THEN TRUE ELSE FALSE END " +
            "FROM TheaterSeat ts " +
            "WHERE ts.theater.id = :theaterId " +
            "AND ts.seatRow = :seatRow " +
            "AND ts.seatNumber = :seatNumber " +
            "AND ts.id != :excludeId")
    boolean existsByTheaterIdAndSeatRowAndSeatNumberExcludingId(
            @Param("theaterId") Long theaterId,
            @Param("seatRow") String seatRow,
            @Param("seatNumber") String seatNumber,
            @Param("excludeId") Long excludeId);

    // 7. Find a specific seat by theater, row, and number
    Optional<TheaterSeat> findByTheaterIdAndSeatRowAndSeatNumber(
            Long theaterId, String seatRow, String seatNumber);

    // 8. Find seats by seat type
    List<TheaterSeat> findBySeatType(SeatType seatType);

    // 9. Find seats by theater and seat type
    List<TheaterSeat> findByTheaterIdAndSeatType(Long theaterId, SeatType seatType);

    // 10. Find active seats by theater and seat type
    List<TheaterSeat> findByTheaterIdAndSeatTypeAndIsActiveTrue(Long theaterId, SeatType seatType);

    // 11. Find seats by row
    List<TheaterSeat> findBySeatRow(String seatRow);

    // 12. Find seats by theater and row
    List<TheaterSeat> findByTheaterIdAndSeatRow(Long theaterId, String seatRow);

    // 13. Count seats by theater
    @Query("SELECT COUNT(ts) FROM TheaterSeat ts WHERE ts.theater.id = :theaterId")
    Long countByTheaterId(@Param("theaterId") Long theaterId);

    // 14. Count active seats by theater
    @Query("SELECT COUNT(ts) FROM TheaterSeat ts WHERE ts.theater.id = :theaterId AND ts.isActive = true")
    Long countActiveSeatsByTheaterId(@Param("theaterId") Long theaterId);

    // 15. Count seats by theater and seat type
    @Query("SELECT COUNT(ts) FROM TheaterSeat ts WHERE ts.theater.id = :theaterId AND ts.seatType = :seatType")
    Long countByTheaterIdAndSeatType(
            @Param("theaterId") Long theaterId,
            @Param("seatType") SeatType seatType);

    // 16. Find seats by theater with pagination (if needed)
    // Already available through Pageable: Page<TheaterSeat> findByTheaterId(Long theaterId, Pageable pageable);

    // 17. Delete all seats by theater ID
    void deleteByTheaterId(Long theaterId);

    // 18. Find seats by list of IDs
    List<TheaterSeat> findByIdIn(List<Long> seatIds);

    // 19. Find seats by theater and list of seat IDs
    List<TheaterSeat> findByTheaterIdAndIdIn(Long theaterId, List<Long> seatIds);

    // 20. Check if any seat is booked for a show (if you have booking system)
    // This would require join with booking/show tables

    // 21. Find available seats (not booked) for a show (if you have booking system)
    // This is more complex and might require custom query with join

    // 22. Get seat statistics by theater
    @Query("SELECT ts.seatType, COUNT(ts) FROM TheaterSeat ts " +
            "WHERE ts.theater.id = :theaterId " +
            "GROUP BY ts.seatType")
    List<Object[]> getSeatStatisticsByTheater(@Param("theaterId") Long theaterId);

    // 23. Find duplicate seats in same theater
    @Query("SELECT ts.seatRow, ts.seatNumber, COUNT(ts) " +
            "FROM TheaterSeat ts " +
            "WHERE ts.theater.id = :theaterId " +
            "GROUP BY ts.seatRow, ts.seatNumber " +
            "HAVING COUNT(ts) > 1")
    List<Object[]> findDuplicateSeatsInTheater(@Param("theaterId") Long theaterId);

    // 24. Deactivate seats by theater ID
    @Query("UPDATE TheaterSeat ts SET ts.isActive = false WHERE ts.theater.id = :theaterId")
    void deactivateAllSeatsByTheaterId(@Param("theaterId") Long theaterId);

    // 25. Activate seats by theater ID
    @Query("UPDATE TheaterSeat ts SET ts.isActive = true WHERE ts.theater.id = :theaterId")
    void activateAllSeatsByTheaterId(@Param("theaterId") Long theaterId);
}