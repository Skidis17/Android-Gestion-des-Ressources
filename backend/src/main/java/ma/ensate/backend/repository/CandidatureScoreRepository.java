package ma.ensate.backend.repository;

import ma.ensate.backend.domain.CandidatureScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureScoreRepository extends JpaRepository<CandidatureScore, Long> {
    List<CandidatureScore> findByCandidatureId(Long candidatureId);
    List<CandidatureScore> findByCandidatureIdAndStageIgnoreCase(Long candidatureId, String stage);
}
