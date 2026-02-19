package moviebookingsystem.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long showId;
    private String movieTitle;
    private String theaterName;
    private List<Long> showSeatIds;
    private List<String> seatNumbers;
    private BigDecimal totalPrice;
    private Boolean paid;
    private LocalDateTime bookingTime;
    private String bookingReference;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}