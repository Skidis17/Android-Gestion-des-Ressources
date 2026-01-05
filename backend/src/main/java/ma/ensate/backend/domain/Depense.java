package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "depenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "besoin_id")
    private Long besoinId;

    private String categorie;

    private BigDecimal montant;

    @Column(name = "date_depense")
    private LocalDate dateDepense;

    private String fournisseur;

    @Column(name = "facture_numero")
    private String factureNumero;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "mode_paiement")
    private String modePaiement;

    @Column(name = "enregistre_par")
    private Long enregistrePar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}