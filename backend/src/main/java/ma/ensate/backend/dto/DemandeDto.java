package ma.ensate.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeDto {
    private Long id;
    private String type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String justificatifUrl;
    private String statut;
    private Long createdBy;
    private String createdByName; // nom + prenom du personnel
    private LocalDateTime createdAt;
}
