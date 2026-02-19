package moviebookingsystem.response;

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
public class BookingResponse {
    private Long id;
    private Long ticketId;
    private Long userId;
    private String userName;
    private Long showId;
    private String movieTitle;
    private String theaterName;
    private Boolean confirmed;
    private String bookingReference;
    private BigDecimal totalPrice;
    private LocalDateTime bookingTime;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}