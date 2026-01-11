package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByAnnee(Integer annee);

    Optional<Budget> findTopByOrderByAnneeDesc();
}
