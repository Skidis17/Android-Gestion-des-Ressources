package ma.ensate.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureStatusHistoryDto {
    private Long id;
    private Long candidatureId;
    private String fromStatus;
    private String toStatus;
    private String reason;
    private String changedBy;
    private LocalDateTime changedAt;
}
