package moviebookingsystem.service;

import moviebookingsystem.entity.Booking;
import moviebookingsystem.entity.Ticket;
import moviebookingsystem.repository.BookingRepository;
import moviebookingsystem.repository.TicketRepository;
import moviebookingsystem.request.BookingRequest;
import moviebookingsystem.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public BookingResponse createBooking(@Valid BookingRequest request) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + request.getTicketId()));

        String bookingReference = generateBookingReference();

        Booking booking = Booking.builder()
                .ticket(ticket)
                .confirmed(request.getConfirmed() != null ? request.getConfirmed() : false)
                .bookingReference(bookingReference)
                .isActive(true)  // Fixed
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking);
    }

    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findByIsActiveTrue();
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return mapToResponse(booking);
    }

    public BookingResponse getBookingByReference(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found with reference: " + bookingReference));
        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse updateBooking(Long id, @Valid BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + request.getTicketId()));

        booking.setTicket(ticket);
        booking.setConfirmed(request.getConfirmed() != null ? request.getConfirmed() : booking.getConfirmed());

        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        booking.setIsActive(false);  // Fixed
        bookingRepository.save(booking);
    }

    @Transactional
    public BookingResponse confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setConfirmed(true);
        Booking confirmedBooking = bookingRepository.save(booking);
        return mapToResponse(confirmedBooking);
    }

    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setConfirmed(false);
        Booking cancelledBooking = bookingRepository.save(booking);
        return mapToResponse(cancelledBooking);
    }

    public List<BookingResponse> getBookingsByTicketId(Long ticketId) {
        List<Booking> bookings = bookingRepository.findByTicketId(ticketId);
        return bookings.stream()
                .filter(Booking::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByTicketUserId(userId);
        return bookings.stream()
                .filter(Booking::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getUserConfirmedBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByTicketUserIdAndConfirmedTrue(userId);
        return bookings.stream()
                .filter(Booking::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByShowId(Long showId) {
        List<Booking> bookings = bookingRepository.findByTicketShowId(showId);
        return bookings.stream()
                .filter(Booking::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getConfirmedBookings() {
        List<Booking> bookings = bookingRepository.findByConfirmedTrue();
        return bookings.stream()
                .filter(Booking::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getPendingBookings() {
        List<Booking> bookings = bookingRepository.findByConfirmedFalse();
        return bookings.stream()
                .filter(Booking::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean isBookingConfirmed(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return Boolean.TRUE.equals(booking.getConfirmed());
    }

    public boolean validateBookingReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference).isPresent();
    }

    private String generateBookingReference() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int)(Math.random() * 1000));
        return "BK" + timestamp.substring(timestamp.length() - 8) + random;
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .ticketId(booking.getTicket().getId())
                .userId(booking.getTicket().getUser() != null ? booking.getTicket().getUser().getId() : null)
                .userName(booking.getTicket().getUser() != null ? booking.getTicket().getUser().getName() : null)
                .showId(booking.getTicket().getShow() != null ? booking.getTicket().getShow().getId() : null)
                .movieTitle(booking.getTicket().getShow() != null && booking.getTicket().getShow().getMovie() != null ?
                        booking.getTicket().getShow().getMovie().getTitle() : null)
                .theaterName(booking.getTicket().getShow() != null && booking.getTicket().getShow().getTheater() != null ?
                        booking.getTicket().getShow().getTheater().getName() : null)
                .confirmed(booking.getConfirmed())
                .bookingReference(booking.getBookingReference())
                .totalPrice(booking.getTicket().getTotalPrice())
                .bookingTime(booking.getTicket().getBookingTime())
                .isActive(booking.getIsActive())  // Fixed
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}