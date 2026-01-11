package ma.ensate.myapplication.repository;

import java.util.List;

import ma.ensate.myapplication.model.CandidatureRecrutement;
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

    public Call<List<CandidatureRecrutement>> selectAccepted(Long recrutementId, boolean sendEmail) {
        return api.selectAcceptedCandidates(recrutementId, sendEmail);
    }
}
