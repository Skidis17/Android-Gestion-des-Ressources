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
public class BesoinDto {
    private Long id;
    private Long personnelId;
    private String typeBesoin;
    private String description;
    private Integer quantite;
    private BigDecimal montantEstime;
    private String priorite;
    private String statut;
    private String commentaireAdmin;
    private Long traitePar;
    private LocalDateTime dateDemande;
    private LocalDateTime dateTraitement;
    private LocalDate dateLivraison;
}