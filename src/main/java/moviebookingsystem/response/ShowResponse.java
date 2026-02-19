package moviebookingsystem.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long theaterId;
    private String theaterName;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer screenNumber;
    private Double basePrice;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}