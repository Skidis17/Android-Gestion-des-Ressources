package ma.ensate.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecrutementDto {
    private Long id;
    private String poste;
    private String typeContrat;
    private String departement;
    private Integer nombrePostes;
    private String description;
    private LocalDate dateOuverture;
    private LocalDate dateCloture;
    private String statut;
    private Long createdBy;
    private LocalDateTime createdAt;
    private String pdfUrl;
}
