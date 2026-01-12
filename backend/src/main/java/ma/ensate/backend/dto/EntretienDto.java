package ma.ensate.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntretienDto {
    private Long id;
    private Long candidatureId;
    private String type;
    private LocalDateTime scheduledAt;
    private String mode;
    private String location;
    private String status;
    private String notes;
    private BigDecimal scoreTotal;
    private String createdBy;
    private LocalDateTime createdAt;
}
