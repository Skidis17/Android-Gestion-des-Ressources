package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Recette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {
    List<Recette> findAllByOrderByIdDesc();
}
