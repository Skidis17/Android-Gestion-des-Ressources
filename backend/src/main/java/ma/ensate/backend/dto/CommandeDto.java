package ma.ensate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeDto {
    private Long id;
    private Long besoinId;
    private String fournisseur;
    private BigDecimal montantTotal;
    private LocalDate dateCommande;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateLivraisonEffective;
    private String statut;
    private String bonCommandeNumero;
    private String notes;
    private Long createdBy;
    private LocalDateTime createdAt;
}