package ma.ensate.backend.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepenseRequest {
    private Long besoinId;
    @NotNull
    private String categorie;
    @NotNull
    private BigDecimal montant;
    @NotNull
    private LocalDate dateDepense;
    private String fournisseur;
    private String factureNumero;
    @NotNull
    private String description;
    private String modePaiement;
    private Long enregistrePar;
}