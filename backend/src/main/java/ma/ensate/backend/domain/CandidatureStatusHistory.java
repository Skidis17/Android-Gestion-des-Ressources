package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidature_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidature_id", nullable = false)
    private Long candidatureId;

    @Column(name = "from_status")
    private String fromStatus;

    @Column(name = "to_status", nullable = false)
    private String toStatus;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
