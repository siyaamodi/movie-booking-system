package moviebookingsystem.service;

import moviebookingsystem.entity.Movie;
import moviebookingsystem.enums.Genre;
import moviebookingsystem.enums.Language;
import moviebookingsystem.repository.MovieRepository;
import moviebookingsystem.request.MovieRequest;
import moviebookingsystem.response.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    @Transactional
    public MovieResponse createMovie(@Valid MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .releaseDate(request.getReleaseDate())
                .director(request.getDirector())
                .cast(request.getCast())
                .certificate(request.getCertificate())
                .posterUrl(request.getPosterUrl())
                .trailerUrl(request.getTrailerUrl())
                .rating(request.getRating())
                .isActive(true)  // Fixed
                .build();

        Movie savedMovie = movieRepository.save(movie);
        return mapToResponse(savedMovie);
    }

    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findByIsActiveTrue();
        return movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return mapToResponse(movie);
    }

    @Transactional
    public MovieResponse updateMovie(Long id, @Valid MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setGenre(request.getGenre());
        movie.setLanguage(request.getLanguage());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDirector(request.getDirector());
        movie.setCast(request.getCast());
        movie.setCertificate(request.getCertificate());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setRating(request.getRating());

        Movie updatedMovie = movieRepository.save(movie);
        return mapToResponse(updatedMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        movie.setIsActive(false);  // Fixed
        movieRepository.save(movie);
    }

    public List<MovieResponse> getMoviesByGenre(Genre genre) {
        List<Movie> movies = movieRepository.findByGenreAndIsActiveTrue(genre);
        return movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getMoviesByLanguage(Language language) {
        List<Movie> movies = movieRepository.findByLanguageAndIsActiveTrue(language);
        return movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> searchMovies(String keyword) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCaseAndIsActiveTrue(keyword);
        return movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getNowShowing() {
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksFromNow = today.plusWeeks(2);

        List<Movie> movies = movieRepository.findByReleaseDateBetweenAndIsActiveTrue(today, twoWeeksFromNow);
        return movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getUpcomingMovies() {
        LocalDate twoWeeksFromNow = LocalDate.now().plusWeeks(2);
        List<Movie> movies = movieRepository.findByReleaseDateAfterAndIsActiveTrue(twoWeeksFromNow);
        return movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MovieResponse mapToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .genre(movie.getGenre())
                .language(movie.getLanguage())
                .releaseDate(movie.getReleaseDate())
                .director(movie.getDirector())
                .cast(movie.getCast())
                .certificate(movie.getCertificate())
                .posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl())
                .rating(movie.getRating())
                .isActive(movie.getIsActive())  // Fixed
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }
}