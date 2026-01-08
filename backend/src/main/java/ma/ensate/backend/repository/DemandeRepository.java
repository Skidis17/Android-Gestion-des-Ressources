package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByStatut(DemandeStatut statut);
}
