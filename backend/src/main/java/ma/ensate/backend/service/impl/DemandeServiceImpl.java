package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.DemandeRepository;
import ma.ensate.backend.service.DemandeService;
import ma.ensate.backend.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository repository;
    private final EmailService emailService;

    @Override
    @Transactional
    public Demande create(Demande demande) {
        if (demande.getStatut() == null) {
            demande.setStatut(DemandeStatut.EN_ATTENTE);
        }
        if (demande.getCreatedAt() == null) {
            demande.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(demande);
    }

    @Override
    public List<Demande> findAll() {
        return repository.findAllWithPersonnel();
    }

    @Override
    public List<Demande> findByStatut(DemandeStatut statut) {
        return repository.findByStatutWithPersonnel(statut);
    }

    @Override
    public Demande findById(Long id) {
        return repository.findByIdWithPersonnel(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found: " + id));
    }

    @Override
    @Transactional
    public Demande updateStatus(Long id, DemandeStatut statut) {
        return updateStatus(id, statut, false);
    }

    @Override
    @Transactional
    public Demande updateStatus(Long id, DemandeStatut statut, boolean sendEmail) {
        Demande demande = findById(id);
        demande.setStatut(statut);
        Demande saved = repository.save(demande);
        
        if (sendEmail && demande.getCreatedByUser() != null) {
            String personnelName = null;
            String personnelEmail = null;
            
            if (demande.getCreatedByUser().getPersonnel() != null) {
                var p = demande.getCreatedByUser().getPersonnel();
                String nom = p.getNom() != null ? p.getNom() : "";
                String prenom = p.getPrenom() != null ? p.getPrenom() : "";
                personnelName = (nom + " " + prenom).trim();
                personnelEmail = p.getEmail();
            }
            
            if (personnelEmail != null && !personnelEmail.isBlank()) {
                try {
                    emailService.sendDemandeStatusChangeEmail(saved, personnelName, personnelEmail);
                } catch (Exception e) {
                    // Log error but don't fail the status update
                    System.err.println("Failed to send email notification: " + e.getMessage());
                }
            }
        }
        
        return saved;
    }
}
