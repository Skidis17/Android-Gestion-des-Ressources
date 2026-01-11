package ma.ensate.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureRecrutementRequest {
    @NotNull
    private Long recrutementId;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @NotNull
    private String cin;

    @NotNull
    @Email
    private String email;

    private String telephone;
    private String cvUrl;
    private String lettreMotivationUrl;
    private String statut;
    private BigDecimal scoreEcrit;
    private BigDecimal scoreOral;
    private String commentaires;
}
