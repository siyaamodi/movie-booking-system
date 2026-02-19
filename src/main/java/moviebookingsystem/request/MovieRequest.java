package moviebookingsystem.request;

import moviebookingsystem.enums.Genre;
import moviebookingsystem.enums.Language;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 60, message = "Duration must be at least 60 minutes")
    @Max(value = 300, message = "Duration must be less than 300 minutes")
    private Integer duration;

    @NotNull(message = "Genre is required")
    private Genre genre;

    @NotNull(message = "Language is required")
    private Language language;

    @NotNull(message = "Release date is required")
    @FutureOrPresent(message = "Release date must be today or in the future")
    private LocalDate releaseDate;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating must be at most 10.0")
    private Double rating;

    @NotBlank(message = "Director is required")
    @Size(min = 2, max = 100, message = "Director name must be between 2 and 100 characters")
    private String director;

    @NotBlank(message = "Cast is required")
    @Size(min = 2, max = 500, message = "Cast must be between 2 and 500 characters")
    private String cast;

    @NotBlank(message = "Certificate is required")
    @Pattern(regexp = "^(U|UA|A|S)$", message = "Certificate must be U, UA, A, or S")
    private String certificate;

    @NotBlank(message = "Poster URL is required")
    @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif|webp)$",
            message = "Poster URL must be a valid image URL")
    private String posterUrl;

    @NotBlank(message = "Trailer URL is required")
    @Pattern(regexp = "^https?://(www\\.)?(youtube\\.com|youtu\\.be)/.*$",
            message = "Trailer URL must be a valid YouTube URL")
    private String trailerUrl;
}