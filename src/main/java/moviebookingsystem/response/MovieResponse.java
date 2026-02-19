package moviebookingsystem.response;

import moviebookingsystem.enums.Genre;
import moviebookingsystem.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private Genre genre;
    private Language language;
    private LocalDate releaseDate;
    private Double rating;
    private String director;
    private String cast;
    private String certificate;
    private String posterUrl;
    private String trailerUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}