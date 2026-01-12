package ma.ensate.backend.repository;

import ma.ensate.backend.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUtilisateurIdOrderByCreatedAtDesc(Long utilisateurId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.utilisateurId = :utilisateurId AND n.estLu = false")
    Long countUnreadByUtilisateurId(Long utilisateurId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.estLu = true WHERE n.utilisateurId = :utilisateurId")
    void markAllAsReadByUtilisateurId(Long utilisateurId);
}
