package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "besoins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Besoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "personnel_id", nullable = false)
    private Long personnelId;

    @Column(name = "type_besoin", nullable = false)
    private String typeBesoin;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private Integer quantite;

    @Column(name = "montant_estime")
    private BigDecimal montantEstime;

    private String priorite;

    private String statut;

    @Column(name = "commentaire_admin", columnDefinition = "TEXT")
    private String commentaireAdmin;

    @Column(name = "traite_par")
    private Long traitePar;

    @Column(name = "date_demande")
    private LocalDateTime dateDemande;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "date_livraison")
    private LocalDate dateLivraison;
}
