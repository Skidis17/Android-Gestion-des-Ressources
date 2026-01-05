package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Long> {
    List<Besoin> findByPersonnelId(Long personnelId);
}
