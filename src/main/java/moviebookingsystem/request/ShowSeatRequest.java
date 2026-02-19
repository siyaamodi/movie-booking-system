package moviebookingsystem.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatRequest {

    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotNull(message = "Theater seat ID is required")
    private Long theaterSeatId;

    @NotNull(message = "Price is required")
    private BigDecimal price;
}