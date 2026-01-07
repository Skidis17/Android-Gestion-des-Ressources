package ma.ensate.backend.repository;

import ma.ensate.backend.domain.CandidatureRecrutement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRecrutementRepository extends JpaRepository<CandidatureRecrutement, Long> {
    List<CandidatureRecrutement> findByRecrutementId(Long recrutementId);
}
