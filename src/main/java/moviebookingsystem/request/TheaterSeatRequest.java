package moviebookingsystem.request;

import moviebookingsystem.enums.SeatType;  // Import your separate enum
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TheaterSeatRequest {

    @NotNull(message = "Theater ID is required")
    private Long theaterId;

    @NotBlank(message = "Seat row is required")
    private String seatRow;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;  // Use your separate enum

    private Boolean isActive = true;
}