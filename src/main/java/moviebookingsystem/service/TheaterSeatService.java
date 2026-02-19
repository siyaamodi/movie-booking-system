package moviebookingsystem.service;

import moviebookingsystem.enums.SeatType;
import moviebookingsystem.request.TheaterSeatRequest;
import moviebookingsystem.response.TheaterSeatResponse;
import lombok.RequiredArgsConstructor;
import moviebookingsystem.entity.TheaterSeat;
import moviebookingsystem.entity.Theater;
import moviebookingsystem.repository.TheaterSeatRepository;
import moviebookingsystem.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterSeatService {
    private final TheaterSeatRepository theaterSeatRepository;
    private final TheaterRepository theaterRepository;

    @Transactional
    public TheaterSeatResponse createTheaterSeat(@Valid TheaterSeatRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + request.getTheaterId()));

        boolean seatExists = theaterSeatRepository.existsByTheaterIdAndSeatRowAndSeatNumber(
                request.getTheaterId(),
                request.getSeatRow(),
                request.getSeatNumber()
        );

        if (seatExists) {
            throw new RuntimeException("Seat " + request.getSeatRow() + "-" + request.getSeatNumber() + " already exists in theater");
        }

        TheaterSeat theaterSeat = new TheaterSeat();
        theaterSeat.setTheater(theater);
        theaterSeat.setSeatNumber(request.getSeatNumber());
        theaterSeat.setSeatRow(request.getSeatRow());
        theaterSeat.setSeatType(request.getSeatType() != null ? request.getSeatType() : SeatType.REGULAR);
        theaterSeat.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        TheaterSeat savedTheaterSeat = theaterSeatRepository.save(theaterSeat);
        return mapToResponse(savedTheaterSeat);
    }

    public List<TheaterSeatResponse> getAllTheaterSeats() {
        return theaterSeatRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TheaterSeatResponse getTheaterSeatById(Long id) {
        TheaterSeat theaterSeat = theaterSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater seat not found with id: " + id));
        return mapToResponse(theaterSeat);
    }

    @Transactional
    public TheaterSeatResponse updateTheaterSeat(Long id, @Valid TheaterSeatRequest request) {
        TheaterSeat theaterSeat = theaterSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater seat not found with id: " + id));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + request.getTheaterId()));

        boolean duplicateSeat = theaterSeatRepository.existsByTheaterIdAndSeatRowAndSeatNumberExcludingId(
                request.getTheaterId(),
                request.getSeatRow(),
                request.getSeatNumber(),
                id);

        if (duplicateSeat) {
            throw new RuntimeException("Seat " + request.getSeatRow() + "-" + request.getSeatNumber() + " already exists in theater");
        }

        theaterSeat.setTheater(theater);
        theaterSeat.setSeatNumber(request.getSeatNumber());
        theaterSeat.setSeatRow(request.getSeatRow());

        if (request.getSeatType() != null) {
            theaterSeat.setSeatType(request.getSeatType());
        }

        if (request.getIsActive() != null) {
            theaterSeat.setIsActive(request.getIsActive());
        }

        TheaterSeat updatedTheaterSeat = theaterSeatRepository.save(theaterSeat);
        return mapToResponse(updatedTheaterSeat);
    }

    @Transactional
    public void deleteTheaterSeat(Long id) {
        if (!theaterSeatRepository.existsById(id)) {
            throw new RuntimeException("Theater seat not found with id: " + id);
        }
        theaterSeatRepository.deleteById(id);
    }

    private TheaterSeatResponse mapToResponse(TheaterSeat theaterSeat) {
        TheaterSeatResponse response = new TheaterSeatResponse();
        response.setId(theaterSeat.getId());
        response.setTheaterId(theaterSeat.getTheater().getId());
        response.setTheaterName(theaterSeat.getTheater().getName());
        response.setSeatRow(theaterSeat.getSeatRow());
        response.setSeatNumber(theaterSeat.getSeatNumber());
        response.setSeatType(theaterSeat.getSeatType());
        response.setIsActive(theaterSeat.getIsActive());
        response.setCreatedAt(theaterSeat.getCreatedAt());
        response.setUpdatedAt(theaterSeat.getUpdatedAt());
        return response;
    }

    public List<TheaterSeatResponse> getSeatsByTheater(Long theaterId) {
        return theaterSeatRepository.findByTheaterId(theaterId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TheaterSeatResponse> getActiveSeatsByTheater(Long theaterId) {
        return theaterSeatRepository.findByTheaterIdAndIsActive(theaterId, true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleSeatStatus(Long seatId, boolean isActive) {
        TheaterSeat seat = theaterSeatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        seat.setIsActive(isActive);
        theaterSeatRepository.save(seat);
    }

    @Transactional
    public TheaterSeatResponse updateSeatType(Long seatId, SeatType seatType) {
        TheaterSeat seat = theaterSeatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        seat.setSeatType(seatType);
        TheaterSeat updatedSeat = theaterSeatRepository.save(seat);
        return mapToResponse(updatedSeat);
    }

    @Transactional
    public List<TheaterSeatResponse> createBulkSeats(Long theaterId, int rows, int seatsPerRow, SeatType defaultType) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        List<TheaterSeat> seats = new java.util.ArrayList<>();

        for (int rowNum = 0; rowNum < rows; rowNum++) {
            char rowChar = (char) ('A' + rowNum);
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                TheaterSeat seat = new TheaterSeat();
                seat.setTheater(theater);
                seat.setSeatRow(String.valueOf(rowChar));
                seat.setSeatNumber(String.valueOf(seatNum));
                seat.setSeatType(defaultType);
                seat.setIsActive(true);
                seats.add(seat);
            }
        }

        List<TheaterSeat> savedSeats = theaterSeatRepository.saveAll(seats);
        return savedSeats.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean isSeatAvailable(Long seatId) {
        TheaterSeat seat = theaterSeatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        return Boolean.TRUE.equals(seat.getIsActive());
    }
}