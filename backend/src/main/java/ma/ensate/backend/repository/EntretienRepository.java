package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    List<Entretien> findByCandidatureId(Long candidatureId);
    long countByStatusIgnoreCase(String status);
}
