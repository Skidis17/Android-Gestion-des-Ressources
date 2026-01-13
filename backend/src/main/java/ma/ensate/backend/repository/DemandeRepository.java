package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByStatut(DemandeStatut statut);
    
    @Query("""
        SELECT DISTINCT d FROM Demande d
        LEFT JOIN FETCH d.createdByUser u
        LEFT JOIN FETCH u.personnel p
        WHERE d.statut = :statut
        ORDER BY d.createdAt DESC
    """)
    List<Demande> findByStatutWithPersonnel(@Param("statut") DemandeStatut statut);
    
    @Query("""
        SELECT DISTINCT d FROM Demande d
        LEFT JOIN FETCH d.createdByUser u
        LEFT JOIN FETCH u.personnel p
        ORDER BY d.createdAt DESC
    """)
    List<Demande> findAllWithPersonnel();
    
    @Query("""
        SELECT d FROM Demande d
        LEFT JOIN FETCH d.createdByUser u
        LEFT JOIN FETCH u.personnel p
        WHERE d.id = :id
    """)
    Optional<Demande> findByIdWithPersonnel(@Param("id") Long id);
}
