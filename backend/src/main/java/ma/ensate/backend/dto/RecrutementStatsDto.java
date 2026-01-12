package ma.ensate.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecrutementStatsDto {
    private int postesOuverts;
    private long totalCandidatures;
    private long entretiensPlanifies;
}
