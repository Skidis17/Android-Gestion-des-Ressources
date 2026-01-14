package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.domain.User;
import ma.ensate.backend.dto.UserItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
    SELECT new ma.ensate.backend.dto.UserItemDto(
        u.id,
        u.username,
        u.email,
        u.role
    )
    FROM User u
    ORDER BY u.username ASC
""")
    List<UserItemDto> findAllUsers();

 @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.personnel p
        WHERE u.id = :id
    """)
    Optional<User> findWithPersonnelById(@Param("id") Long id);

}
