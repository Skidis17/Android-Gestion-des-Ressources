package ma.ensate.backend.repository;

import ma.ensate.backend.domain.EntretienScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntretienScoreRepository extends JpaRepository<EntretienScore, Long> {
    List<EntretienScore> findByEntretienId(Long entretienId);
}
