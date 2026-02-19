package moviebookingsystem.response;

import moviebookingsystem.enums.SeatStatus;
import moviebookingsystem.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatResponse {
    private Long id;
    private Long showId;
    private Long theaterSeatId;
    private String seatNumber;
    private SeatType seatType;
    private String rowNumber;
    private BigDecimal price;
    private SeatStatus status;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}