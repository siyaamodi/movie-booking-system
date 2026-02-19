package moviebookingsystem.service;

import moviebookingsystem.entity.ShowSeat;
import moviebookingsystem.entity.Show;
import moviebookingsystem.entity.TheaterSeat;
import moviebookingsystem.enums.SeatStatus;
import moviebookingsystem.repository.ShowSeatRepository;
import moviebookingsystem.repository.ShowRepository;
import moviebookingsystem.repository.TheaterSeatRepository;
import moviebookingsystem.request.ShowSeatRequest;
import moviebookingsystem.response.ShowSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowSeatService {
    private final ShowSeatRepository showSeatRepository;
    private final ShowRepository showRepository;
    private final TheaterSeatRepository theaterSeatRepository;

    @Transactional
    public ShowSeatResponse createShowSeat(@Valid ShowSeatRequest request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + request.getShowId()));

        TheaterSeat theaterSeat = theaterSeatRepository.findById(request.getTheaterSeatId())
                .orElseThrow(() -> new RuntimeException("Theater seat not found with id: " + request.getTheaterSeatId()));

        ShowSeat showSeat = ShowSeat.builder()
                .show(show)
                .theaterSeat(theaterSeat)
                .price(request.getPrice())
                .status(SeatStatus.AVAILABLE)
                .isActive(true)  // Fixed
                .build();

        ShowSeat savedShowSeat = showSeatRepository.save(showSeat);
        return mapToResponse(savedShowSeat);
    }

    @Transactional
    public List<ShowSeatResponse> createBulkShowSeats(@Valid List<ShowSeatRequest> requests) {
        List<ShowSeat> showSeats = requests.stream().map(request -> {
            Show show = showRepository.findById(request.getShowId())
                    .orElseThrow(() -> new RuntimeException("Show not found with id: " + request.getShowId()));

            TheaterSeat theaterSeat = theaterSeatRepository.findById(request.getTheaterSeatId())
                    .orElseThrow(() -> new RuntimeException("Theater seat not found with id: " + request.getTheaterSeatId()));

            return ShowSeat.builder()
                    .show(show)
                    .theaterSeat(theaterSeat)
                    .price(request.getPrice())
                    .status(SeatStatus.AVAILABLE)
                    .isActive(true)  // Fixed
                    .build();
        }).collect(Collectors.toList());

        List<ShowSeat> savedShowSeats = showSeatRepository.saveAll(showSeats);
        return savedShowSeats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowSeatResponse> getAllShowSeats() {
        List<ShowSeat> showSeats = showSeatRepository.findByIsActiveTrue();
        return showSeats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ShowSeatResponse getShowSeatById(Long id) {
        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show seat not found with id: " + id));
        return mapToResponse(showSeat);
    }

    @Transactional
    public ShowSeatResponse updateShowSeat(Long id, @Valid ShowSeatRequest request) {
        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show seat not found with id: " + id));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + request.getShowId()));

        TheaterSeat theaterSeat = theaterSeatRepository.findById(request.getTheaterSeatId())
                .orElseThrow(() -> new RuntimeException("Theater seat not found with id: " + request.getTheaterSeatId()));

        showSeat.setShow(show);
        showSeat.setTheaterSeat(theaterSeat);
        showSeat.setPrice(request.getPrice());

        ShowSeat updatedShowSeat = showSeatRepository.save(showSeat);
        return mapToResponse(updatedShowSeat);
    }

    @Transactional
    public void deleteShowSeat(Long id) {
        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show seat not found with id: " + id));
        showSeat.setIsActive(false);  // Fixed
        showSeatRepository.save(showSeat);
    }

    public List<ShowSeatResponse> getShowSeatsByShowId(Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowIdAndIsActiveTrue(showId);
        return showSeats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowSeatResponse> getAvailableSeatsByShowId(Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowIdAndStatus(showId, SeatStatus.AVAILABLE);
        return showSeats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowSeatResponse> getBookedSeatsByShowId(Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowIdAndStatus(showId, SeatStatus.BOOKED);
        return showSeats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShowSeatResponse updateSeatStatus(Long showSeatId, SeatStatus status) {
        ShowSeat showSeat = showSeatRepository.findById(showSeatId)
                .orElseThrow(() -> new RuntimeException("Show seat not found with id: " + showSeatId));

        showSeat.setStatus(status);
        ShowSeat updatedSeat = showSeatRepository.save(showSeat);
        return mapToResponse(updatedSeat);
    }

    @Transactional
    public void bulkUpdateSeatStatus(List<Long> showSeatIds, SeatStatus status) {
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatIds);
        showSeats.forEach(seat -> seat.setStatus(status));
        showSeatRepository.saveAll(showSeats);
    }

    @Transactional
    public ShowSeatResponse updateSeatPrice(Long showSeatId, BigDecimal price) {
        ShowSeat showSeat = showSeatRepository.findById(showSeatId)
                .orElseThrow(() -> new RuntimeException("Show seat not found with id: " + showSeatId));

        showSeat.setPrice(price);
        ShowSeat updatedSeat = showSeatRepository.save(showSeat);
        return mapToResponse(updatedSeat);
    }

    @Transactional
    public void resetAllSeatsForShow(Long showId) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowIdAndIsActiveTrue(showId);
        showSeats.forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
        showSeatRepository.saveAll(showSeats);
    }

    private ShowSeatResponse mapToResponse(ShowSeat showSeat) {
        return ShowSeatResponse.builder()
                .id(showSeat.getId())
                .showId(showSeat.getShow().getId())
                .theaterSeatId(showSeat.getTheaterSeat().getId())
                .seatNumber(showSeat.getTheaterSeat().getSeatNumber())
                .seatType(showSeat.getTheaterSeat().getSeatType())
                .rowNumber(showSeat.getTheaterSeat().getSeatRow())  // Fixed
                .price(showSeat.getPrice())
                .status(showSeat.getStatus())
                .isActive(showSeat.getIsActive())  // Fixed
                .createdAt(showSeat.getCreatedAt())
                .updatedAt(showSeat.getUpdatedAt())
                .build();
    }
}