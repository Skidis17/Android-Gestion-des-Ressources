package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recrutements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recrutement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "poste", nullable = false, columnDefinition = "TEXT")
    private String poste;
    
    @Column(name = "type_contrat", columnDefinition = "TEXT")
    private String typeContrat;
    
    @Column(name = "departement", columnDefinition = "TEXT")
    private String departement;
    
    @Column(name = "nombre_postes")
    private Integer nombrePostes;
    
    @Lob
    @Column(name = "description")
    private String description;
    
    @Column(name = "date_ouverture")
    private LocalDate dateOuverture;
    
    @Column(name = "date_cloture")
    private LocalDate dateCloture;
    
    @Column(name = "statut", columnDefinition = "TEXT")
    private String statut;
    
    @Column(name = "created_by")
    private Long createdBy; 
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "pdf_url")
    private String pdfUrl;
}
