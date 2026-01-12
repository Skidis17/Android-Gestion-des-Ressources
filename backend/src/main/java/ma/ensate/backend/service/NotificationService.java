package ma.ensate.backend.service;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Notification;
import ma.ensate.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public List<Notification> getUserNotifications(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdOrderByCreatedAtDesc(utilisateurId);
    }
    
    public Long getUnreadCount(Long utilisateurId) {
        return notificationRepository.countUnreadByUtilisateurId(utilisateurId);
    }
    
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setEstLu(true);
        return notificationRepository.save(notification);
    }
    
    @Transactional
    public void markAllAsRead(Long utilisateurId) {
        notificationRepository.markAllAsReadByUtilisateurId(utilisateurId);
    }
    
    @Transactional
    public Notification createNotification(Long utilisateurId, String titre, String message, String type) {
        Notification notification = new Notification();
        notification.setUtilisateurId(utilisateurId);
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType(type);
        notification.setEstLu(false);
        return notificationRepository.save(notification);
    }
}
