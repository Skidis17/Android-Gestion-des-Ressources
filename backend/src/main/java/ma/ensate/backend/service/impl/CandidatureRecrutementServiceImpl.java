package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.CandidatureRecrutementRepository;
import ma.ensate.backend.repository.RecrutementRepository;
import ma.ensate.backend.service.EmailService;
import ma.ensate.backend.service.CandidatureRecrutementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatureRecrutementServiceImpl implements CandidatureRecrutementService {

    private final CandidatureRecrutementRepository candidatureRecrutementRepository;
    private final RecrutementRepository recrutementRepository;
    private final EmailService emailService;

    @Override
    public List<CandidatureRecrutement> findAll() {
        return candidatureRecrutementRepository.findAll();
    }

    @Override
    public CandidatureRecrutement findById(Long id) {
        return candidatureRecrutementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature not found: " + id));
    }

    @Override
    @Transactional
    public CandidatureRecrutement create(CandidatureRecrutement candidature) {
        if (!recrutementRepository.existsById(candidature.getRecrutementId())) {
            throw new ResourceNotFoundException("Recrutement not found: " + candidature.getRecrutementId());
        }
        if (candidature.getStatut() == null) {
            candidature.setStatut("EN_ATTENTE");
        }
        if (candidature.getDateCandidature() == null) {
            candidature.setDateCandidature(LocalDateTime.now());
        }
        return candidatureRecrutementRepository.save(candidature);
    }

    @Override
    @Transactional
    public CandidatureRecrutement update(Long id, CandidatureRecrutement update) {
        CandidatureRecrutement existing = findById(id);
        existing.setNom(update.getNom());
        existing.setPrenom(update.getPrenom());
        existing.setCin(update.getCin());
        existing.setEmail(update.getEmail());
        existing.setTelephone(update.getTelephone());
        existing.setCvUrl(update.getCvUrl());
        existing.setLettreMotivationUrl(update.getLettreMotivationUrl());
        if (update.getStatut() != null) {
            existing.setStatut(update.getStatut());
        }
        existing.setScoreEcrit(update.getScoreEcrit());
        existing.setScoreOral(update.getScoreOral());
        existing.setCommentaires(update.getCommentaires());
        return candidatureRecrutementRepository.save(existing);
    }

    @Override
    @Transactional
    public CandidatureRecrutement updateStatus(Long id, String statut, boolean sendEmail) {
        CandidatureRecrutement existing = findById(id);
        existing.setStatut(statut);
        CandidatureRecrutement saved = candidatureRecrutementRepository.save(existing);
        if (sendEmail && "RETENU".equalsIgnoreCase(statut)) {
            Recrutement recrutement = recrutementRepository.findById(saved.getRecrutementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recrutement not found: " + saved.getRecrutementId()));
            emailService.sendAcceptanceEmail(saved, recrutement);
        }
        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CandidatureRecrutement existing = findById(id);
        candidatureRecrutementRepository.delete(existing);
    }

    @Override
    public List<CandidatureRecrutement> findByRecrutementId(Long recrutementId) {
        return candidatureRecrutementRepository.findByRecrutementId(recrutementId);
    }

    @Override
    @Transactional
    public List<CandidatureRecrutement> selectAcceptedCandidates(Long recrutementId, boolean sendEmail) {
        Recrutement recrutement = recrutementRepository.findById(recrutementId)
                .orElseThrow(() -> new ResourceNotFoundException("Recrutement not found: " + recrutementId));
        int slots = recrutement.getNombrePostes() != null ? recrutement.getNombrePostes() : 1;
        List<CandidatureRecrutement> candidatures = candidatureRecrutementRepository.findByRecrutementId(recrutementId);

        candidatures.sort(Comparator
                .comparing(CandidatureRecrutement::getScoreEcrit, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(CandidatureRecrutement::getScoreOral, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(CandidatureRecrutement::getDateCandidature, Comparator.nullsLast(Comparator.naturalOrder())));

        for (int i = 0; i < candidatures.size(); i++) {
            CandidatureRecrutement c = candidatures.get(i);
            if (i < slots) {
                c.setStatut("RETENU");
                if (sendEmail) {
                    emailService.sendAcceptanceEmail(c, recrutement);
                }
            } else {
                c.setStatut("REFUSE");
            }
        }
        return candidatureRecrutementRepository.saveAll(candidatures);
    }
}
