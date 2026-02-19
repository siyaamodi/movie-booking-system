package moviebookingsystem.response;

import moviebookingsystem.enums.SeatType;  // Import your separate enum
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TheaterSeatResponse {

    private Long id;
    private Long theaterId;
    private String theaterName;
    private String seatRow;
    private String seatNumber;
    private SeatType seatType;  // Use your separate enum
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}