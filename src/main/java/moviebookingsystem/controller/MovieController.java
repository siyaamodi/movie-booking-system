package moviebookingsystem.controller;

import moviebookingsystem.request.MovieRequest;
import moviebookingsystem.response.MovieResponse;
import moviebookingsystem.service.MovieService;
import moviebookingsystem.enums.Genre;
import moviebookingsystem.enums.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        MovieResponse response = movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        List<MovieResponse> movies = movieService.getAllMovies();
        return movies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(movies)
                : ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        MovieResponse response = movieService.getMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id,
                                                     @Valid @RequestBody MovieRequest request) {
        MovieResponse response = movieService.updateMovie(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.OK).body("Movie deleted successfully");
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(@PathVariable Genre genre) {
        List<MovieResponse> movies = movieService.getMoviesByGenre(genre);
        return movies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(movies)
                : ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieResponse>> getMoviesByLanguage(@PathVariable Language language) {
        List<MovieResponse> movies = movieService.getMoviesByLanguage(language);
        return movies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(movies)
                : ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(@RequestParam String keyword) {
        List<MovieResponse> movies = movieService.searchMovies(keyword);
        return movies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(movies)
                : ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/now-showing")
    public ResponseEntity<List<MovieResponse>> getNowShowing() {
        List<MovieResponse> movies = movieService.getNowShowing();
        return movies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(movies)
                : ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieResponse>> getUpcomingMovies() {
        List<MovieResponse> movies = movieService.getUpcomingMovies();
        return movies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(movies)
                : ResponseEntity.status(HttpStatus.OK).body(movies);
    }
}