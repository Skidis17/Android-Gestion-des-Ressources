package ma.ensate.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Besoin;
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
        
        // Define valid transitions
        switch (current) {
            case "EN_ATTENTE":
                return "VALIDÉ".equals(next) || "REFUSÉ".equals(next);
            case "VALIDÉ":
                return "APPROUVÉ".equals(next) || "REFUSÉ".equals(next);
            case "APPROUVÉ":
                return "TRANSMIS_A_ECO".equals(next);
            case "REFUSÉ":
            case "TRANSMIS_A_ECO":
                return false; // Terminal states
            default:
                return false;
        }
    }

    @Override
    public List<Besoin> findByPersonnelId(Long personnelId) {
        return besoinRepository.findByPersonnelId(personnelId);
    }
}