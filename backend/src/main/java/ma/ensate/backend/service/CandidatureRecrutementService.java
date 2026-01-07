package ma.ensate.backend.service;

import ma.ensate.backend.domain.CandidatureRecrutement;

import java.util.List;

public interface CandidatureRecrutementService {
    List<CandidatureRecrutement> findAll();
    CandidatureRecrutement findById(Long id);
    CandidatureRecrutement create(CandidatureRecrutement candidature);
    CandidatureRecrutement update(Long id, CandidatureRecrutement candidature);
    CandidatureRecrutement updateStatus(Long id, String statut, boolean sendEmail);
    void delete(Long id);
    List<CandidatureRecrutement> findByRecrutementId(Long recrutementId);
    List<CandidatureRecrutement> selectAcceptedCandidates(Long recrutementId, boolean sendEmail);
}
