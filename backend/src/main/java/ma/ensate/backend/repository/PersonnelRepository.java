package ma.ensate.backend.repository;

import ma.ensate.backend.dto.PersonnelOptionDto;
import ma.ensate.backend.domain.Personnel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    @Query("""
        SELECT new ma.ensate.backend.dto.PersonnelOptionDto(
            p.id,
            CONCAT(p.nom, ' ', p.prenom),
            p.email
        )
        FROM Personnel p
        ORDER BY p.nom ASC
    """)
    List<PersonnelOptionDto> findAllOptions();

    @Query("""
        SELECT new ma.ensate.backend.dto.PersonnelOptionDto(
            p.id,
            CONCAT(p.nom, ' ', p.prenom),
            p.email
        )
        FROM Personnel p
        WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY p.nom ASC
    """)
    List<PersonnelOptionDto> searchOptions(@Param("q") String q);
}
