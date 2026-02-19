package moviebookingsystem.service;

import moviebookingsystem.entity.Show;
import moviebookingsystem.entity.Movie;
import moviebookingsystem.entity.Theater;
import moviebookingsystem.repository.ShowRepository;
import moviebookingsystem.repository.MovieRepository;
import moviebookingsystem.repository.TheaterRepository;
import moviebookingsystem.request.ShowRequest;
import moviebookingsystem.response.ShowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    @Transactional
    public ShowResponse createShow(@Valid ShowRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + request.getMovieId()));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + request.getTheaterId()));

        Show show = Show.builder()
                .movie(movie)
                .theater(theater)
                .showDate(request.getShowDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .screenNumber(request.getScreenNumber())
                .basePrice(request.getBasePrice())
                .isActive(true)  // Fixed
                .build();

        Show savedShow = showRepository.save(show);
        return mapToResponse(savedShow);
    }

    public List<ShowResponse> getAllShows() {
        List<Show> shows = showRepository.findByIsActiveTrue();
        return shows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ShowResponse getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));
        return mapToResponse(show);
    }

    @Transactional
    public ShowResponse updateShow(Long id, @Valid ShowRequest request) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + request.getMovieId()));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + request.getTheaterId()));

        show.setMovie(movie);
        show.setTheater(theater);
        show.setShowDate(request.getShowDate());
        show.setStartTime(request.getStartTime());
        show.setEndTime(request.getEndTime());
        show.setScreenNumber(request.getScreenNumber());
        show.setBasePrice(request.getBasePrice());

        Show updatedShow = showRepository.save(show);
        return mapToResponse(updatedShow);
    }

    @Transactional
    public void deleteShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));
        show.setIsActive(false);  // Fixed
        showRepository.save(show);
    }

    public List<ShowResponse> getShowsByMovieId(Long movieId) {
        List<Show> shows = showRepository.findByMovieIdAndIsActiveTrue(movieId);
        return shows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByTheaterId(Long theaterId) {
        List<Show> shows = showRepository.findByTheaterIdAndIsActiveTrue(theaterId);
        return shows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByMovieAndTheater(Long movieId, Long theaterId) {
        List<Show> shows = showRepository.findByMovieIdAndTheaterIdAndIsActiveTrue(movieId, theaterId);
        return shows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByDate(LocalDate date) {
        List<Show> shows = showRepository.findByShowDateAndIsActiveTrue(date);
        return shows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getUpcomingShows() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Show> allShows = showRepository.findByIsActiveTrue();

        return allShows.stream()
                .filter(show -> {
                    if (show.getShowDate().isAfter(today)) {
                        return true;
                    } else if (show.getShowDate().isEqual(today) &&
                            show.getStartTime().isAfter(now)) {
                        return true;
                    }
                    return false;
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ShowResponse mapToResponse(Show show) {
        return ShowResponse.builder()
                .id(show.getId())
                .movieId(show.getMovie().getId())
                .movieTitle(show.getMovie().getTitle())
                .theaterId(show.getTheater().getId())
                .theaterName(show.getTheater().getName())
                .showDate(show.getShowDate())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .screenNumber(show.getScreenNumber())
                .basePrice(show.getBasePrice())
                .isActive(show.getIsActive())  // Fixed
                .createdAt(show.getCreatedAt())
                .updatedAt(show.getUpdatedAt())
                .build();
    }
}