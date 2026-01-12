package ma.ensate.myapplication.repository;

import java.util.List;

import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.model.CandidatureScore;
import ma.ensate.myapplication.model.CandidatureScoreRequest;
import ma.ensate.myapplication.model.CandidatureStatusHistory;
import ma.ensate.myapplication.model.Entretien;
import ma.ensate.myapplication.model.EntretienRequest;
import ma.ensate.myapplication.model.EntretienScore;
import ma.ensate.myapplication.model.EntretienScoreRequest;
import ma.ensate.myapplication.model.StatusChangeRequest;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

public class CandidatureRecrutementRepository {
    private final ApiService api = RetrofitClient.api();

    public Call<List<CandidatureRecrutement>> getByRecrutement(Long recrutementId) {
        return api.getCandidaturesByRecrutement(recrutementId);
    }

    public Call<CandidatureRecrutement> getById(Long id) {
        return api.getCandidature(id);
    }

    public Call<CandidatureRecrutement> create(CandidatureRecrutement c) {
        return api.createCandidature(c);
    }

    public Call<CandidatureRecrutement> updateStatus(Long id, String statut, boolean sendEmail) {
        return api.updateCandidatureStatus(id, statut, sendEmail);
    }

    public Call<CandidatureRecrutement> updateStatusDetail(Long id, StatusChangeRequest request) {
        return api.updateCandidatureStatusDetail(id, request);
    }

    public Call<List<CandidatureStatusHistory>> getHistory(Long id) {
        return api.getCandidatureHistory(id);
    }

    public Call<List<CandidatureScore>> getScores(Long id) {
        return api.getCandidatureScores(id);
    }

    public Call<CandidatureScore> addScore(Long id, CandidatureScoreRequest request) {
        return api.addCandidatureScore(id, request);
    }

    public Call<List<Entretien>> getEntretiens(Long id) {
        return api.getEntretiens(id);
    }

    public Call<Entretien> createEntretien(Long id, EntretienRequest request) {
        return api.createEntretien(id, request);
    }

    public Call<List<EntretienScore>> getEntretienScores(Long entretienId) {
        return api.getEntretienScores(entretienId);
    }

    public Call<EntretienScore> addEntretienScore(Long entretienId, EntretienScoreRequest request) {
        return api.addEntretienScore(entretienId, request);
    }

    public Call<List<CandidatureRecrutement>> selectAccepted(Long recrutementId, boolean sendEmail) {
        return api.selectAcceptedCandidates(recrutementId, sendEmail);
    }
}
