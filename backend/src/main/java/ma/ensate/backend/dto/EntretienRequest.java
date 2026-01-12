package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntretienRequest {
    @NotNull
    private String type;
    @NotNull
    private LocalDateTime scheduledAt;
    private String mode;
    private String location;
    private String status;
    private String notes;
    private String createdBy;
}
