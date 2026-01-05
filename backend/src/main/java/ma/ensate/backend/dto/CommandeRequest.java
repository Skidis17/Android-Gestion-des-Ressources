package ma.ensate.backend.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeRequest {
    @NotNull
    private Long besoinId;

    @NotNull
    private String fournisseur;

    @NotNull
    private BigDecimal montantTotal;

    @NotNull
    private LocalDate dateCommande;

    private LocalDate dateLivraisonPrevue;
    private LocalDate dateLivraisonEffective;
    private String statut;
    private String bonCommandeNumero;
    private String notes;
    private Long createdBy;
}