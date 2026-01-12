package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "entretiens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entretien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidature_id", nullable = false)
    private Long candidatureId;

    @Column(nullable = false)
    private String type;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    private String mode;

    private String location;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "score_total", precision = 5, scale = 2)
    private BigDecimal scoreTotal;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
