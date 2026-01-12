package ma.ensate.backend.repository;

import ma.ensate.backend.domain.CandidatureStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureStatusHistoryRepository extends JpaRepository<CandidatureStatusHistory, Long> {
    List<CandidatureStatusHistory> findByCandidatureIdOrderByChangedAtDesc(Long candidatureId);
}
