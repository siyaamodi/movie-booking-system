package moviebookingsystem.service;

import moviebookingsystem.entity.Payment;
import moviebookingsystem.entity.Ticket;
import moviebookingsystem.entity.Booking;
import moviebookingsystem.enums.PaymentMethod;
import moviebookingsystem.enums.PaymentStatus;
import moviebookingsystem.repository.PaymentRepository;
import moviebookingsystem.repository.TicketRepository;
import moviebookingsystem.repository.BookingRepository;
import moviebookingsystem.request.PaymentRequest;
import moviebookingsystem.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public PaymentResponse createPayment(@Valid PaymentRequest request) {
        if (request.getTransactionId() != null &&
                paymentRepository.existsByTransactionId(request.getTransactionId())) {
            throw new RuntimeException("Transaction ID already exists");
        }

        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + request.getTicketId()));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + request.getBookingId()));

        String transactionId = request.getTransactionId() != null ?
                request.getTransactionId() :
                "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .transactionId(transactionId)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(request.getStatus() != null ? request.getStatus() : PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .ticket(ticket)
                .booking(booking)
                .isActive(true)  // Fixed
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findByIsActiveTrue();
        return payments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction id: " + transactionId));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByTicketId(Long ticketId) {
        Payment payment = paymentRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ticket id: " + ticketId));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking id: " + bookingId));
        return mapToResponse(payment);
    }

    @Transactional
    public PaymentResponse updatePayment(Long id, @Valid PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + request.getTicketId()));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + request.getBookingId()));

        if (request.getTransactionId() != null &&
                !request.getTransactionId().equals(payment.getTransactionId()) &&
                paymentRepository.existsByTransactionId(request.getTransactionId())) {
            throw new RuntimeException("Transaction ID already exists");
        }

        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(request.getStatus());
        payment.setTicket(ticket);
        payment.setBooking(booking);

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        payment.setStatus(status);
        payment.setPaymentDate(LocalDateTime.now());

        if (status == PaymentStatus.COMPLETED) {
            Ticket ticket = payment.getTicket();
            ticket.setPaid(true);
            ticketRepository.save(ticket);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    @Transactional
    public PaymentResponse processPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        Ticket ticket = payment.getTicket();
        ticket.setPaid(true);
        ticketRepository.save(ticket);

        Payment processedPayment = paymentRepository.save(payment);
        return mapToResponse(processedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        payment.setIsActive(false);  // Fixed
        paymentRepository.save(payment);
    }

    @Transactional
    public PaymentResponse refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setPaymentDate(LocalDateTime.now());

        Payment refundedPayment = paymentRepository.save(payment);
        return mapToResponse(refundedPayment);
    }

    @Transactional
    public PaymentResponse cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setPaymentDate(LocalDateTime.now());

        Payment cancelledPayment = paymentRepository.save(payment);
        return mapToResponse(cancelledPayment);
    }

    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .filter(Payment::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByMethod(PaymentMethod method) {
        List<Payment> payments = paymentRepository.findByPaymentMethod(method);
        return payments.stream()
                .filter(Payment::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
                .filter(Payment::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        List<Payment> payments = paymentRepository.findByTicketUserId(userId);
        return payments.stream()
                .filter(Payment::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getUserPaymentsByStatus(Long userId, PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByTicketUserIdAndStatus(userId, status);
        return payments.stream()
                .filter(Payment::getIsActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Double getTotalRevenue() {
        List<Payment> completedPayments = paymentRepository.findByStatus(PaymentStatus.COMPLETED);
        return completedPayments.stream()
                .filter(Payment::getIsActive)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public Double getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED && payment.getIsActive())
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .ticketId(payment.getTicket() != null ? payment.getTicket().getId() : null)
                .bookingId(payment.getBooking() != null ? payment.getBooking().getId() : null)
                .userId(payment.getTicket() != null && payment.getTicket().getUser() != null ?
                        payment.getTicket().getUser().getId() : null)
                .userName(payment.getTicket() != null && payment.getTicket().getUser() != null ?
                        payment.getTicket().getUser().getName() : null)
                .isActive(payment.getIsActive())  // Fixed
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}