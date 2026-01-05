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
public class DepenseDto {
    private Long id;
    private Long besoinId;
    private String categorie;
    private BigDecimal montant;
    private LocalDate dateDepense;
    private String fournisseur;
    private String factureNumero;
    private String description;
    private String modePaiement;
    private Long enregistrePar;
    private LocalDateTime createdAt;
}