package moviebookingsystem.controller;

import moviebookingsystem.request.BookingRequest;
import moviebookingsystem.response.BookingResponse;
import moviebookingsystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<BookingResponse> getBookingByReference(@PathVariable String bookingReference) {
        BookingResponse response = bookingService.getBookingByReference(bookingReference);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByTicket(@PathVariable Long ticketId) {
        List<BookingResponse> bookings = bookingService.getBookingsByTicketId(ticketId);
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUser(@PathVariable Long userId) {
        List<BookingResponse> bookings = bookingService.getBookingsByUserId(userId);
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/user/{userId}/confirmed")
    public ResponseEntity<List<BookingResponse>> getUserConfirmedBookings(@PathVariable Long userId) {
        List<BookingResponse> bookings = bookingService.getUserConfirmedBookings(userId);
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByShow(@PathVariable Long showId) {
        List<BookingResponse> bookings = bookingService.getBookingsByShowId(showId);
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/confirmed")
    public ResponseEntity<List<BookingResponse>> getConfirmedBookings() {
        List<BookingResponse> bookings = bookingService.getConfirmedBookings();
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BookingResponse>> getPendingBookings() {
        List<BookingResponse> bookings = bookingService.getPendingBookings();
        return bookings.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings)
                : ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id,
                                                         @Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.updateBooking(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long id) {
        BookingResponse response = bookingService.confirmBooking(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) {
        BookingResponse response = bookingService.cancelBooking(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(HttpStatus.OK).body("Booking deleted successfully");
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> checkBookingStatus(@PathVariable Long id) {
        boolean isConfirmed = bookingService.isBookingConfirmed(id);
        return ResponseEntity.status(HttpStatus.OK).body(isConfirmed);
    }

    @GetMapping("/validate/{bookingReference}")
    public ResponseEntity<Boolean> validateBooking(@PathVariable String bookingReference) {
        boolean isValid = bookingService.validateBookingReference(bookingReference);
        return ResponseEntity.status(HttpStatus.OK).body(isValid);
    }
}