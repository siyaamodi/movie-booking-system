package moviebookingsystem.controller;

import moviebookingsystem.request.PaymentRequest;
import moviebookingsystem.response.PaymentResponse;
import moviebookingsystem.service.PaymentService;
import moviebookingsystem.enums.PaymentMethod;
import moviebookingsystem.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return payments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments)
                : ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentResponse response = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<PaymentResponse> getPaymentByTicketId(@PathVariable Long ticketId) {
        PaymentResponse response = paymentService.getPaymentByTicketId(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(@PathVariable Long bookingId) {
        PaymentResponse response = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id,
                                                         @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.updatePayment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable Long id,
                                                               @RequestParam PaymentStatus status) {
        PaymentResponse response = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id) {
        PaymentResponse response = paymentService.processPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        PaymentResponse response = paymentService.refundPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable Long id) {
        PaymentResponse response = paymentService.cancelPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Payment deleted successfully");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);
        return payments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments)
                : ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByMethod(@PathVariable PaymentMethod method) {
        List<PaymentResponse> payments = paymentService.getPaymentsByMethod(method);
        return payments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments)
                : ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUser(@PathVariable Long userId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByUserId(userId);
        return payments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments)
                : ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getUserPaymentsByStatus(
            @PathVariable Long userId,
            @PathVariable PaymentStatus status) {
        List<PaymentResponse> payments = paymentService.getUserPaymentsByStatus(userId, status);
        return payments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments)
                : ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PaymentResponse> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return payments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments)
                : ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue() {
        Double totalRevenue = paymentService.getTotalRevenue();
        return ResponseEntity.status(HttpStatus.OK).body(totalRevenue);
    }

    @GetMapping("/revenue/date-range")
    public ResponseEntity<Double> getRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double revenue = paymentService.getRevenueByDateRange(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }
}