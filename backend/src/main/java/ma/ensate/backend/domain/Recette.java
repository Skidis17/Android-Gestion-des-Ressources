package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recettes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;

    private String categorie;

    private BigDecimal montant;

    @Column(name = "date_recette")
    private LocalDate dateRecette;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "reference_document")
    private String referenceDocument;

    @Column(name = "enregistre_par")
    private Long enregistrePar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
