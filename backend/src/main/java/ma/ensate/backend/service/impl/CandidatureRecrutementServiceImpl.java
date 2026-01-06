package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.CandidatureRecrutementRepository;
import ma.ensate.backend.repository.RecrutementRepository;
import ma.ensate.backend.service.CandidatureRecrutementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatureRecrutementServiceImpl implements CandidatureRecrutementService {

    private final CandidatureRecrutementRepository candidatureRecrutementRepository;
    private final RecrutementRepository recrutementRepository;

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
    public void delete(Long id) {
        CandidatureRecrutement existing = findById(id);
        candidatureRecrutementRepository.delete(existing);
    }

    @Override
    public List<CandidatureRecrutement> findByRecrutementId(Long recrutementId) {
        return candidatureRecrutementRepository.findByRecrutementId(recrutementId);
    }
}
