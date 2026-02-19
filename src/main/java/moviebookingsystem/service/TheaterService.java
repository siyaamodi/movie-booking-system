package moviebookingsystem.service;

import moviebookingsystem.entity.Theater;
import moviebookingsystem.repository.TheaterRepository;
import moviebookingsystem.request.TheaterRequest;
import moviebookingsystem.response.TheaterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;

    @Transactional
    public TheaterResponse createTheater(@Valid TheaterRequest request) {
        Theater theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .totalScreens(request.getTotalScreens())
                .isActive(true)
                .build();

        Theater savedTheater = theaterRepository.save(theater);
        return mapToResponse(savedTheater);
    }

    public List<TheaterResponse> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findByIsActiveTrue();
        return theaters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TheaterResponse getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + id));
        return mapToResponse(theater);
    }

    @Transactional
    public TheaterResponse updateTheater(Long id, @Valid TheaterRequest request) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + id));

        theater.setName(request.getName());
        theater.setLocation(request.getLocation());
        theater.setCity(request.getCity());
        theater.setState(request.getState());
        theater.setZipCode(request.getZipCode());
        theater.setTotalScreens(request.getTotalScreens());

        Theater updatedTheater = theaterRepository.save(theater);
        return mapToResponse(updatedTheater);
    }

    @Transactional
    public void deleteTheater(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + id));
        theater.setIsActive(false);
        theaterRepository.save(theater);
    }

    public List<TheaterResponse> getTheatersByCity(String city) {
        List<Theater> theaters = theaterRepository.findByCityAndIsActiveTrue(city);
        return theaters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TheaterResponse mapToResponse(Theater theater) {
        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .city(theater.getCity())
                .state(theater.getState())
                .zipCode(theater.getZipCode())
                .totalScreens(theater.getTotalScreens())
                .isActive(theater.getIsActive())
                .createdAt(theater.getCreatedAt())
                .updatedAt(theater.getUpdatedAt())
                .build();
    }
}