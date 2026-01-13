package ma.ensate.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.Enum.Role;
import ma.ensate.backend.domain.Besoin;
import ma.ensate.backend.domain.User;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.BesoinRepository;
import ma.ensate.backend.service.BesoinService;

@Service
@RequiredArgsConstructor
public class BesoinServiceImpl implements BesoinService {

    private final BesoinRepository besoinRepository;

    @Override
    public List<Besoin> findAll() {
        return besoinRepository.findAll();
    }

    @Override
    public Besoin findById(Long id) {
        return besoinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Besoin not found: " + id));
    }

    @Override
    @Transactional
    public Besoin create(Besoin besoin) {
        if (besoin.getStatut() == null) {
            besoin.setStatut("EN_ATTENTE");
        }
        if (besoin.getDateDemande() == null) {
            besoin.setDateDemande(LocalDateTime.now());
        }
        return besoinRepository.save(besoin);
    }

    @Override
    @Transactional
    public Besoin update(Long id, Besoin update) {
        Besoin existing = findById(id);
        
        // Only allow update if status is EN_ATTENTE
        if (!"EN_ATTENTE".equals(existing.getStatut())) {
            throw new IllegalStateException("Le besoin ne peut être modifié qu'en statut EN_ATTENTE. Statut actuel: " + existing.getStatut());
        }
        
        existing.setTypeBesoin(update.getTypeBesoin());
        existing.setDescription(update.getDescription());
        existing.setQuantite(update.getQuantite());
        existing.setMontantEstime(update.getMontantEstime());
        existing.setPriorite(update.getPriorite());
        existing.setCommentaireAdmin(update.getCommentaireAdmin());
        existing.setDateLivraison(update.getDateLivraison());
        // Don't allow status change through update - use changeStatus method instead
        return besoinRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Besoin existing = findById(id);
        
        // Only allow delete if status is EN_ATTENTE
        if (!"EN_ATTENTE".equals(existing.getStatut())) {
            throw new IllegalStateException("Le besoin ne peut être supprimé qu'en statut EN_ATTENTE. Statut actuel: " + existing.getStatut());
        }
        
        besoinRepository.delete(existing);
    }

    @Override
    @Transactional
    public Besoin changeStatus(Long id, String statut, Long traitePar, String commentaireAdmin) {
        Besoin existing = findById(id);
        String currentStatut = existing.getStatut() != null ? existing.getStatut() : "EN_ATTENTE";
        
        // Validate status transitions
        if (!isValidStatusTransition(currentStatut, statut)) {
            throw new IllegalStateException("Transition de statut invalide: " + currentStatut + " -> " + statut);
        }
        
        existing.setStatut(statut);
        if (traitePar != null) {
            existing.setTraitePar(traitePar);
        }
        if (commentaireAdmin != null && !commentaireAdmin.trim().isEmpty()) {
            existing.setCommentaireAdmin(commentaireAdmin);
        }
        existing.setDateTraitement(LocalDateTime.now());
        return besoinRepository.save(existing);
    }
    
    private boolean isValidStatusTransition(String current, String next) {
        // Allow same status (for adding comments without changing status)
        if (current.equals(next)) {
            return true;
        }
        
        // Define valid transitions based on the workflow:
        // EN_ATTENTE → VALIDÉ/REFUSÉ (by Directeur_adjoint)
        // VALIDÉ → APPROUVÉ/REFUSÉ (by Directeur)
        // APPROUVÉ → TRANSMIS (by Secretaire_general)
        switch (current) {
            case "EN_ATTENTE":
                return "VALIDÉ".equals(next) || "REFUSÉ".equals(next);
            case "VALIDÉ":
                return "APPROUVÉ".equals(next) || "REFUSÉ".equals(next);
            case "APPROUVÉ":
                return "TRANSMIS".equals(next);
            case "REFUSÉ":
            case "TRANSMIS":
                return false; // Terminal states
            default:
                return false;
        }
    }

    @Override
    public List<Besoin> findByPersonnelId(Long personnelId) {
        return besoinRepository.findByPersonnelId(personnelId);
    }
    
    @Override
    @Transactional
    public Besoin changeStatusWithUser(Long id, String statut, User currentUser, String commentaireAdmin) {
        Besoin existing = findById(id);
        String currentStatut = existing.getStatut() != null ? existing.getStatut() : "EN_ATTENTE";
        
        // Validate status transitions
        if (!isValidStatusTransition(currentStatut, statut)) {
            throw new IllegalStateException("Transition de statut invalide: " + currentStatut + " -> " + statut);
        }
        
        // Validate role permissions for status changes
        if (!hasPermissionForStatusChange(currentUser, currentStatut, statut)) {
            throw new IllegalStateException("Vous n'avez pas les permissions pour effectuer cette transition de statut");
        }
        
        existing.setStatut(statut);
        if (currentUser.getPersonnelId() != null) {
            existing.setTraitePar(currentUser.getPersonnelId());
        }
        if (commentaireAdmin != null && !commentaireAdmin.trim().isEmpty()) {
            existing.setCommentaireAdmin(commentaireAdmin);
        }
        existing.setDateTraitement(LocalDateTime.now());
        return besoinRepository.save(existing);
    }
    
    private boolean hasPermissionForStatusChange(User user, String currentStatut, String nextStatut) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        
        Role userRole = user.getRole();
        
        // Allow same status change (just adding/editing comment) for ALL roles
        if (currentStatut.equals(nextStatut)) {
            return true;
        }
        
        // EN_ATTENTE → VALIDÉ/REFUSÉ: only Directeur_adjoint
        if ("EN_ATTENTE".equals(currentStatut)) {
            if ("VALIDÉ".equals(nextStatut) || "REFUSÉ".equals(nextStatut)) {
                return userRole == Role.Directeur_adjoint || userRole == Role.admin;
            }
        }
        
        // VALIDÉ → APPROUVÉ/REFUSÉ: only Directeur
        if ("VALIDÉ".equals(currentStatut)) {
            if ("APPROUVÉ".equals(nextStatut) || "REFUSÉ".equals(nextStatut)) {
                return userRole == Role.directeur || userRole == Role.admin;
            }
        }
        
        // APPROUVÉ → TRANSMIS: only Secretaire_general
        if ("APPROUVÉ".equals(currentStatut) && "TRANSMIS".equals(nextStatut)) {
            return userRole == Role.secretaire_general || userRole == Role.admin;
        }
        
        // Allow admin to make any valid transition
        return userRole == Role.admin;
    }

    @Override
    @Transactional
    public Besoin markAsTransmitted(Long id) {
        Besoin besoin = findById(id);
        besoin.setStatut("TRANSMIS");
        besoin.setDateTraitement(LocalDateTime.now());
        return besoinRepository.save(besoin);
    }
}