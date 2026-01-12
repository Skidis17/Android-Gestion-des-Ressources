package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeRequest {
    @NotNull
    private String type;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotNull
    private String motif;

    private String justificatifUrl;

    @NotNull
    private Long createdBy;
}
