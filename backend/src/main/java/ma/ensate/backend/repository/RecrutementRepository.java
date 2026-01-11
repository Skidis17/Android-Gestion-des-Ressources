package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Recrutement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecrutementRepository extends JpaRepository<Recrutement, Long> {
}
