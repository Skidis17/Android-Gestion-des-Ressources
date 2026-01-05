package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "besoin_id", nullable = false)
    private Long besoinId;

    private String fournisseur;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    @Column(name = "date_commande")
    private LocalDate dateCommande;

    @Column(name = "date_livraison_prevue")
    private LocalDate dateLivraisonPrevue;

    @Column(name = "date_livraison_effective")
    private LocalDate dateLivraisonEffective;

    private String statut;

    @Column(name = "bon_commande_numero")
    private String bonCommandeNumero;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}