package ma.ensate.myapplication.repository;

import java.util.List;

import ma.ensate.myapplication.model.Recrutement;
import ma.ensate.myapplication.model.RecrutementPipeline;
import ma.ensate.myapplication.model.CandidatureRanking;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

public class RecrutementRepository {
    private final ApiService api = RetrofitClient.api();

    public Call<List<Recrutement>> getRecrutements() { return api.getRecrutements(); }
    public Call<Recrutement> getRecrutement(Long id) { return api.getRecrutement(id); }
    public Call<Recrutement> createRecrutement(Recrutement r) { return api.createRecrutement(r); }
    public Call<Recrutement> updateRecrutement(Long id, Recrutement r) { return api.updateRecrutement(id, r); }
    public Call<Recrutement> changeStatus(Long id, String statut) { return api.changeRecrutementStatus(id, statut); }
    public Call<RecrutementPipeline> getPipeline(Long id) { return api.getRecrutementPipeline(id); }
    public Call<List<CandidatureRanking>> getRankings(Long id) { return api.getRecrutementRankings(id); }
}
