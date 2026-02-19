package moviebookingsystem.controller;

import moviebookingsystem.request.ShowSeatRequest;
import moviebookingsystem.response.ShowSeatResponse;
import moviebookingsystem.service.ShowSeatService;
import moviebookingsystem.enums.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/show-seats")
@RequiredArgsConstructor
public class ShowSeatController {
    private final ShowSeatService showSeatService;

    @PostMapping
    public ResponseEntity<ShowSeatResponse> createShowSeat(@Valid @RequestBody ShowSeatRequest request) {
        ShowSeatResponse response = showSeatService.createShowSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ShowSeatResponse>> createBulkShowSeats(@Valid @RequestBody List<ShowSeatRequest> requests) {
        List<ShowSeatResponse> responses = showSeatService.createBulkShowSeats(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping
    public ResponseEntity<List<ShowSeatResponse>> getAllShowSeats() {
        List<ShowSeatResponse> showSeats = showSeatService.getAllShowSeats();
        return showSeats.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(showSeats)
                : ResponseEntity.status(HttpStatus.OK).body(showSeats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowSeatResponse> getShowSeatById(@PathVariable Long id) {
        ShowSeatResponse response = showSeatService.getShowSeatById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowSeatResponse> updateShowSeat(@PathVariable Long id,
                                                           @Valid @RequestBody ShowSeatRequest request) {
        ShowSeatResponse response = showSeatService.updateShowSeat(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShowSeat(@PathVariable Long id) {
        showSeatService.deleteShowSeat(id);
        return ResponseEntity.status(HttpStatus.OK).body("Show seat deleted successfully");
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<ShowSeatResponse>> getShowSeatsByShow(@PathVariable Long showId) {
        List<ShowSeatResponse> showSeats = showSeatService.getShowSeatsByShowId(showId);
        return showSeats.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(showSeats)
                : ResponseEntity.status(HttpStatus.OK).body(showSeats);
    }

    @GetMapping("/show/{showId}/available")
    public ResponseEntity<List<ShowSeatResponse>> getAvailableSeats(@PathVariable Long showId) {
        List<ShowSeatResponse> showSeats = showSeatService.getAvailableSeatsByShowId(showId);
        return showSeats.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(showSeats)
                : ResponseEntity.status(HttpStatus.OK).body(showSeats);
    }

    @GetMapping("/show/{showId}/booked")
    public ResponseEntity<List<ShowSeatResponse>> getBookedSeats(@PathVariable Long showId) {
        List<ShowSeatResponse> showSeats = showSeatService.getBookedSeatsByShowId(showId);
        return showSeats.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(showSeats)
                : ResponseEntity.status(HttpStatus.OK).body(showSeats);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ShowSeatResponse> updateSeatStatus(@PathVariable Long id,
                                                             @RequestParam SeatStatus status) {
        ShowSeatResponse response = showSeatService.updateSeatStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/bulk/status")
    public ResponseEntity<String> bulkUpdateSeatStatus(@RequestParam List<Long> seatIds,
                                                       @RequestParam SeatStatus status) {
        showSeatService.bulkUpdateSeatStatus(seatIds, status);
        return ResponseEntity.status(HttpStatus.OK).body("Seat statuses updated successfully");
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<ShowSeatResponse> updateSeatPrice(@PathVariable Long id,
                                                            @RequestParam BigDecimal price) {
        ShowSeatResponse response = showSeatService.updateSeatPrice(id, price);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/show/{showId}/reset")
    public ResponseEntity<String> resetSeatsForShow(@PathVariable Long showId) {
        showSeatService.resetAllSeatsForShow(showId);
        return ResponseEntity.status(HttpStatus.OK).body("All seats reset to available");
    }
}