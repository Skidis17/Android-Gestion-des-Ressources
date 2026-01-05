package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    List<Depense> findByBesoinId(Long besoinId);
}
