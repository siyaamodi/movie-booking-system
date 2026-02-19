package moviebookingsystem.controller;

import moviebookingsystem.request.TheaterSeatRequest;
import moviebookingsystem.response.TheaterSeatResponse;
import moviebookingsystem.service.TheaterSeatService;
import moviebookingsystem.enums.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/theater-seats")
@RequiredArgsConstructor
public class TheaterSeatController {
    private final TheaterSeatService theaterSeatService;

    @PostMapping
    public ResponseEntity<TheaterSeatResponse> create(@Valid @RequestBody TheaterSeatRequest request) {
        TheaterSeatResponse response = theaterSeatService.createTheaterSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TheaterSeatResponse>> getAll() {
        List<TheaterSeatResponse> theaterSeats = theaterSeatService.getAllTheaterSeats();
        return theaterSeats.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(theaterSeats)
                : ResponseEntity.status(HttpStatus.OK).body(theaterSeats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterSeatResponse> getById(@PathVariable Long id) {
        TheaterSeatResponse response = theaterSeatService.getTheaterSeatById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterSeatResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TheaterSeatRequest request) {
        TheaterSeatResponse response = theaterSeatService.updateTheaterSeat(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        theaterSeatService.deleteTheaterSeat(id);
        return ResponseEntity.status(HttpStatus.OK).body("Theater seat deleted successfully");
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<TheaterSeatResponse>> getByTheater(@PathVariable Long theaterId) {
        List<TheaterSeatResponse> theaterSeats = theaterSeatService.getSeatsByTheater(theaterId);
        return theaterSeats.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(theaterSeats)
                : ResponseEntity.status(HttpStatus.OK).body(theaterSeats);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> toggleStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        theaterSeatService.toggleSeatStatus(id, isActive);
        return ResponseEntity.status(HttpStatus.OK).body("Seat status updated successfully");
    }

    @PatchMapping("/{id}/seat-type")
    public ResponseEntity<TheaterSeatResponse> updateSeatType(
            @PathVariable Long id,
            @RequestParam SeatType seatType) {
        TheaterSeatResponse response = theaterSeatService.updateSeatType(id, seatType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/bulk/{theaterId}")
    public ResponseEntity<List<TheaterSeatResponse>> createBulk(
            @PathVariable Long theaterId,
            @RequestParam int rows,
            @RequestParam int seatsPerRow,
            @RequestParam(defaultValue = "REGULAR") SeatType seatType) {
        List<TheaterSeatResponse> responses = theaterSeatService.createBulkSeats(theaterId, rows, seatsPerRow, seatType);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}