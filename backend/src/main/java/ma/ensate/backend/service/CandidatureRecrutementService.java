package ma.ensate.backend.service;

import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.domain.CandidatureScore;
import ma.ensate.backend.domain.CandidatureStatusHistory;
import ma.ensate.backend.domain.Entretien;
import ma.ensate.backend.domain.EntretienScore;

import java.util.List;

public interface CandidatureRecrutementService {
    List<CandidatureRecrutement> findAll();
    CandidatureRecrutement findById(Long id);
    CandidatureRecrutement create(CandidatureRecrutement candidature);
    CandidatureRecrutement update(Long id, CandidatureRecrutement candidature);
    CandidatureRecrutement updateStatus(Long id, String statut, boolean sendEmail);
    CandidatureRecrutement updateStatus(Long id, String statut, boolean sendEmail, String reason, String changedBy);
    void delete(Long id);
    List<CandidatureRecrutement> findByRecrutementId(Long recrutementId);
    List<CandidatureRecrutement> selectAcceptedCandidates(Long recrutementId, boolean sendEmail);

    List<CandidatureStatusHistory> history(Long candidatureId);
    List<CandidatureScore> listScores(Long candidatureId);
    CandidatureScore addScore(Long candidatureId, CandidatureScore score);
    List<Entretien> listEntretiens(Long candidatureId);
    Entretien createEntretien(Long candidatureId, Entretien entretien, boolean sendEmail);
    List<EntretienScore> listEntretienScores(Long entretienId);
    EntretienScore addEntretienScore(Long entretienId, EntretienScore score);
}
