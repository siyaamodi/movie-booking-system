package moviebookingsystem.controller;

import moviebookingsystem.request.ShowRequest;
import moviebookingsystem.response.ShowResponse;
import moviebookingsystem.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowResponse> createShow(@Valid @RequestBody ShowRequest request) {
        ShowResponse response = showService.createShow(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ShowResponse>> getAllShows() {
        List<ShowResponse> shows = showService.getAllShows();
        return shows.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(shows)
                : ResponseEntity.status(HttpStatus.OK).body(shows);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> getShowById(@PathVariable Long id) {
        ShowResponse response = showService.getShowById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowResponse> updateShow(@PathVariable Long id,
                                                   @Valid @RequestBody ShowRequest request) {
        ShowResponse response = showService.updateShow(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.status(HttpStatus.OK).body("Show deleted successfully");
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowResponse>> getShowsByMovie(@PathVariable Long movieId) {
        List<ShowResponse> shows = showService.getShowsByMovieId(movieId);
        return shows.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(shows)
                : ResponseEntity.status(HttpStatus.OK).body(shows);
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ShowResponse>> getShowsByTheater(@PathVariable Long theaterId) {
        List<ShowResponse> shows = showService.getShowsByTheaterId(theaterId);
        return shows.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(shows)
                : ResponseEntity.status(HttpStatus.OK).body(shows);
    }

    @GetMapping("/movie/{movieId}/theater/{theaterId}")
    public ResponseEntity<List<ShowResponse>> getShowsByMovieAndTheater(
            @PathVariable Long movieId,
            @PathVariable Long theaterId) {
        List<ShowResponse> shows = showService.getShowsByMovieAndTheater(movieId, theaterId);
        return shows.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(shows)
                : ResponseEntity.status(HttpStatus.OK).body(shows);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShowResponse>> getShowsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShowResponse> shows = showService.getShowsByDate(date);
        return shows.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(shows)
                : ResponseEntity.status(HttpStatus.OK).body(shows);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ShowResponse>> getUpcomingShows() {
        List<ShowResponse> shows = showService.getUpcomingShows();
        return shows.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(shows)
                : ResponseEntity.status(HttpStatus.OK).body(shows);
    }
}