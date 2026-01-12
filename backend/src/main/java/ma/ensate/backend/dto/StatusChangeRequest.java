package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusChangeRequest {
    @NotNull
    private String statut;
    private String reason;
    private Boolean sendEmail;
    private String changedBy;
}
