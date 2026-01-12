package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.domain.CandidatureScore;
import ma.ensate.backend.domain.CandidatureStatusHistory;
import ma.ensate.backend.domain.Entretien;
import ma.ensate.backend.domain.EntretienScore;
import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.CandidatureScoreRepository;
import ma.ensate.backend.repository.CandidatureStatusHistoryRepository;
import ma.ensate.backend.repository.CandidatureRecrutementRepository;
import ma.ensate.backend.repository.EntretienRepository;
import ma.ensate.backend.repository.EntretienScoreRepository;
import ma.ensate.backend.repository.RecrutementRepository;
import ma.ensate.backend.service.EmailService;
import ma.ensate.backend.service.CandidatureRecrutementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatureRecrutementServiceImpl implements CandidatureRecrutementService {

    private final CandidatureRecrutementRepository candidatureRecrutementRepository;
    private final RecrutementRepository recrutementRepository;
    private final CandidatureScoreRepository candidatureScoreRepository;
    private final CandidatureStatusHistoryRepository candidatureStatusHistoryRepository;
    private final EntretienRepository entretienRepository;
    private final EntretienScoreRepository entretienScoreRepository;
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
        if (candidatureRecrutementRepository.existsByRecrutementIdAndEmail(candidature.getRecrutementId(), candidature.getEmail())) {
            throw new IllegalArgumentException("Candidature already exists for this recrutement and email");
        }
        if (candidature.getStatut() == null) {
            candidature.setStatut("EN_ATTENTE");
        }
        if (candidature.getDateCandidature() == null) {
            candidature.setDateCandidature(LocalDateTime.now());
        }
        CandidatureRecrutement saved = candidatureRecrutementRepository.save(candidature);
        recordStatusChange(saved, null, saved.getStatut(), null, null);
        return saved;
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
        return updateStatus(id, statut, sendEmail, null, null);
    }

    @Override
    @Transactional
    public CandidatureRecrutement updateStatus(Long id, String statut, boolean sendEmail, String reason, String changedBy) {
        CandidatureRecrutement existing = findById(id);
        String fromStatus = existing.getStatut();
        existing.setStatut(statut);
        CandidatureRecrutement saved = candidatureRecrutementRepository.save(existing);
        recordStatusChange(saved, fromStatus, statut, reason, changedBy);
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
            String fromStatus = c.getStatut();
            if (i < slots) {
                c.setStatut("RETENU");
                if (sendEmail) {
                    emailService.sendAcceptanceEmail(c, recrutement);
                }
            } else {
                c.setStatut("REFUSE");
            }
            recordStatusChange(c, fromStatus, c.getStatut(), "Selection auto", "system");
        }
        return candidatureRecrutementRepository.saveAll(candidatures);
    }

    @Override
    public List<CandidatureStatusHistory> history(Long candidatureId) {
        return candidatureStatusHistoryRepository.findByCandidatureIdOrderByChangedAtDesc(candidatureId);
    }

    @Override
    public List<CandidatureScore> listScores(Long candidatureId) {
        return candidatureScoreRepository.findByCandidatureId(candidatureId);
    }

    @Override
    @Transactional
    public CandidatureScore addScore(Long candidatureId, CandidatureScore score) {
        CandidatureRecrutement candidature = findById(candidatureId);
        score.setCandidatureId(candidatureId);
        score.setCreatedAt(LocalDateTime.now());
        CandidatureScore saved = candidatureScoreRepository.save(score);
        updateStageAggregates(candidatureId, candidature);
        return saved;
    }

    @Override
    public List<Entretien> listEntretiens(Long candidatureId) {
        return entretienRepository.findByCandidatureId(candidatureId);
    }

    @Override
    @Transactional
    public Entretien createEntretien(Long candidatureId, Entretien entretien, boolean sendEmail) {
        CandidatureRecrutement candidature = findById(candidatureId);
        entretien.setCandidatureId(candidatureId);
        if (entretien.getStatus() == null) {
            entretien.setStatus("PLANIFIE");
        }
        if (entretien.getScheduledAt() == null) {
            entretien.setScheduledAt(LocalDateTime.now());
        }
        if (entretien.getCreatedAt() == null) {
            entretien.setCreatedAt(LocalDateTime.now());
        }
        Entretien saved = entretienRepository.save(entretien);
        if (sendEmail) {
            Recrutement recrutement = recrutementRepository.findById(candidature.getRecrutementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recrutement not found: " + candidature.getRecrutementId()));
            emailService.sendInterviewScheduledEmail(candidature, recrutement, saved);
        }
        return saved;
    }

    @Override
    public List<EntretienScore> listEntretienScores(Long entretienId) {
        return entretienScoreRepository.findByEntretienId(entretienId);
    }

    @Override
    @Transactional
    public EntretienScore addEntretienScore(Long entretienId, EntretienScore score) {
        Entretien entretien = entretienRepository.findById(entretienId)
                .orElseThrow(() -> new ResourceNotFoundException("Entretien not found: " + entretienId));
        score.setEntretienId(entretienId);
        score.setCreatedAt(LocalDateTime.now());
        EntretienScore saved = entretienScoreRepository.save(score);
        updateEntretienTotal(entretienId, entretien);
        return saved;
    }

    private void recordStatusChange(CandidatureRecrutement candidature, String fromStatus, String toStatus, String reason, String changedBy) {
        if (candidature == null || toStatus == null) return;
        CandidatureStatusHistory history = CandidatureStatusHistory.builder()
                .candidatureId(candidature.getId())
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .reason(reason)
                .changedBy(changedBy)
                .changedAt(LocalDateTime.now())
                .build();
        candidatureStatusHistoryRepository.save(history);
    }

    private void updateStageAggregates(Long candidatureId, CandidatureRecrutement candidature) {
        updateStageAverage(candidatureId, "ECRIT", candidature, true);
        updateStageAverage(candidatureId, "ORAL", candidature, false);
        candidatureRecrutementRepository.save(candidature);
    }

    private void updateStageAverage(Long candidatureId, String stage, CandidatureRecrutement candidature, boolean isEcrit) {
        List<CandidatureScore> scores = candidatureScoreRepository.findByCandidatureIdAndStageIgnoreCase(candidatureId, stage);
        BigDecimal avg = weightedAverage(scores);
        if (isEcrit) {
            candidature.setScoreEcrit(avg);
        } else {
            candidature.setScoreOral(avg);
        }
    }

    private void updateEntretienTotal(Long entretienId, Entretien entretien) {
        List<EntretienScore> scores = entretienScoreRepository.findByEntretienId(entretienId);
        BigDecimal avg = weightedAverage(scores);
        entretien.setScoreTotal(avg);
        entretienRepository.save(entretien);
    }

    private BigDecimal weightedAverage(List<? extends Object> scores) {
        if (scores == null || scores.isEmpty()) return null;
        BigDecimal weightedSum = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (Object s : scores) {
            BigDecimal score;
            BigDecimal weight;
            if (s instanceof CandidatureScore cs) {
                score = cs.getScore();
                weight = cs.getWeight();
            } else if (s instanceof EntretienScore es) {
                score = es.getScore();
                weight = es.getWeight();
            } else {
                continue;
            }
            if (score == null) continue;
            BigDecimal w = weight != null ? weight : BigDecimal.ONE;
            weightedSum = weightedSum.add(score.multiply(w));
            totalWeight = totalWeight.add(w);
        }
        if (totalWeight.compareTo(BigDecimal.ZERO) == 0) return null;
        return weightedSum.divide(totalWeight, 2, RoundingMode.HALF_UP);
    }
}
