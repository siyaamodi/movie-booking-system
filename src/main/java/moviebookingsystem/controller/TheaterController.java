package moviebookingsystem.controller;

import moviebookingsystem.request.TheaterRequest;
import moviebookingsystem.response.TheaterResponse;
import moviebookingsystem.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<TheaterResponse> createTheater(@Valid @RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.createTheater(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> getAllTheaters() {
        List<TheaterResponse> theaters = theaterService.getAllTheaters();
        return theaters.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(theaters)
                : ResponseEntity.status(HttpStatus.OK).body(theaters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponse> getTheaterById(@PathVariable Long id) {
        TheaterResponse response = theaterService.getTheaterById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterResponse> updateTheater(@PathVariable Long id,
                                                         @Valid @RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.updateTheater(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.status(HttpStatus.OK).body("Theater deleted successfully");
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterResponse>> getTheatersByCity(@PathVariable String city) {
        List<TheaterResponse> theaters = theaterService.getTheatersByCity(city);
        return theaters.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(theaters)
                : ResponseEntity.status(HttpStatus.OK).body(theaters);
    }
}