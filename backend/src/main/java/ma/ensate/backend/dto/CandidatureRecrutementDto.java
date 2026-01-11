package ma.ensate.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureRecrutementDto {
    private Long id;
    private Long recrutementId;
    private String nom;
    private String prenom;
    private String cin;
    private String email;
    private String telephone;
    private String cvUrl;
    private String lettreMotivationUrl;
    private String statut;
    private BigDecimal scoreEcrit;
    private BigDecimal scoreOral;
    private String commentaires;
    private LocalDateTime dateCandidature;
}
