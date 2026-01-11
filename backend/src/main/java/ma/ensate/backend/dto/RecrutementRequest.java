package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecrutementRequest {
    @NotNull
    private String poste;

    @NotNull
    private String typeContrat;

    @NotNull
    private String departement;

    private Integer nombrePostes;
    private String description;

    @NotNull
    private LocalDate dateOuverture;

    @NotNull
    private LocalDate dateCloture;

    private String statut;
    private Long createdBy;
}
