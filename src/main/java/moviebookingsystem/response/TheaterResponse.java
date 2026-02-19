package moviebookingsystem.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterResponse {
    private Long id;
    private String name;
    private String location;
    private String city;
    private String state;
    private String zipCode;
    private Integer totalScreens;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}