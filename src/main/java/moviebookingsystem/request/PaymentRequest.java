package moviebookingsystem.request;

import moviebookingsystem.enums.PaymentMethod;
import moviebookingsystem.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String transactionId;  // Optional, will be generated if not provided

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus status;  // Optional, defaults to PENDING

    @NotNull(message = "Ticket ID is required")
    private Long ticketId;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;
}