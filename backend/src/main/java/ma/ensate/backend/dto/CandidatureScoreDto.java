package ma.ensate.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureScoreDto {
    private Long id;
    private Long candidatureId;
    private String stage;
    private String criterion;
    private BigDecimal score;
    private BigDecimal weight;
    private String reviewer;
    private String notes;
    private LocalDateTime createdAt;
}
