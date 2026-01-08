package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures_recrutement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureRecrutement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recrutement_id", nullable = false)
    private Long recrutementId;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String cin;

    @Column(nullable = false)
    private String email;

    private String telephone;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "lettre_motivation_url")
    private String lettreMotivationUrl;

    private String statut;

    @Column(name = "score_ecrit", precision = 5, scale = 2)
    private BigDecimal scoreEcrit;

    @Column(name = "score_oral", precision = 5, scale = 2)
    private BigDecimal scoreOral;

    @Column(columnDefinition = "TEXT")
    private String commentaires;

    @Column(name = "date_candidature")
    private LocalDateTime dateCandidature;
}
