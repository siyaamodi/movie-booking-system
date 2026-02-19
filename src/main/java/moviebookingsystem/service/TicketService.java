package moviebookingsystem.service;

import moviebookingsystem.request.TicketRequest;
import moviebookingsystem.response.TicketResponse;
import lombok.RequiredArgsConstructor;
import moviebookingsystem.entity.Ticket;
import moviebookingsystem.entity.User;
import moviebookingsystem.entity.Show;
import moviebookingsystem.entity.ShowSeat;
import moviebookingsystem.entity.Booking;
import moviebookingsystem.enums.SeatStatus;
import moviebookingsystem.repository.TicketRepository;
import moviebookingsystem.repository.UserRepository;
import moviebookingsystem.repository.ShowRepository;
import moviebookingsystem.repository.ShowSeatRepository;
import moviebookingsystem.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public TicketResponse createTicket(@Valid TicketRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + request.getShowId()));

        List<ShowSeat> showSeats = request.getShowSeatIds().stream()
                .map(id -> showSeatRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Show seat not found with id: " + id)))
                .collect(Collectors.toList());

        for (ShowSeat seat : showSeats) {
            if (!seat.getStatus().equals(SeatStatus.AVAILABLE)) {
                throw new RuntimeException("Seat " + seat.getTheaterSeat().getSeatNumber() + " is not available");
            }
        }

        BigDecimal totalPrice = showSeats.stream()
                .map(ShowSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Ticket ticket = Ticket.builder()
                .user(user)
                .show(show)
                .showSeats(showSeats)
                .totalPrice(totalPrice)
                .paid(false)
                .bookingTime(LocalDateTime.now())
                .isActive(true)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        Booking booking = Booking.builder()
                .ticket(savedTicket)
                .confirmed(false)
                .bookingReference("BK" + System.currentTimeMillis())
                .isActive(true)
                .build();

        bookingRepository.save(booking);

        showSeats.forEach(seat -> {
            seat.setStatus(SeatStatus.BOOKED);
            showSeatRepository.save(seat);
        });

        return mapToResponse(savedTicket);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        return mapToResponse(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(Long id, @Valid TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        // Update logic here if needed

        Ticket updatedTicket = ticketRepository.save(ticket);
        return mapToResponse(updatedTicket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        ticket.setIsActive(false);
        ticketRepository.save(ticket);
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUser().getId())
                .userName(ticket.getUser().getName())
                .showId(ticket.getShow().getId())
                .movieTitle(ticket.getShow().getMovie().getTitle())
                .theaterName(ticket.getShow().getTheater().getName())
                .showSeatIds(ticket.getShowSeats().stream()
                        .map(ShowSeat::getId)
                        .collect(Collectors.toList()))
                .seatNumbers(ticket.getShowSeats().stream()
                        .map(seat -> seat.getTheaterSeat().getSeatNumber())
                        .collect(Collectors.toList()))
                .totalPrice(ticket.getTotalPrice())
                .paid(ticket.getPaid())
                .bookingTime(ticket.getBookingTime())
                .bookingReference(ticket.getBooking() != null ? ticket.getBooking().getBookingReference() : null)
                .isActive(ticket.getIsActive())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}