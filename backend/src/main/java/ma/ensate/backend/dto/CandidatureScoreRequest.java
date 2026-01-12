package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureScoreRequest {
    @NotNull
    private String stage;
    @NotNull
    private String criterion;
    @NotNull
    private BigDecimal score;
    private BigDecimal weight;
    private String reviewer;
    private String notes;
}
