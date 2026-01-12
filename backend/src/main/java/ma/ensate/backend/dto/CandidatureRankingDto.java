package ma.ensate.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureRankingDto {
    private Long candidatureId;
    private String nom;
    private String prenom;
    private String email;
    private String statut;
    private BigDecimal scoreEcrit;
    private BigDecimal scoreOral;
    private BigDecimal interviewScore;
    private BigDecimal totalScore;
}
