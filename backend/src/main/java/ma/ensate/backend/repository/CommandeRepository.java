package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByBesoinId(Long besoinId);
}
