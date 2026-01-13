package ma.ensate.myapplication.repository;

import java.util.List;

import ma.ensate.myapplication.model.Demande;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

public class DemandeRepository {
    private final ApiService api = RetrofitClient.api();

    public Call<List<Demande>> getDemandes(String statut) { return api.getDemandes(statut); }
    public Call<Demande> getDemande(Long id) { return api.getDemande(id); }
    public Call<Demande> createDemande(Demande demande) { return api.createDemande(demande); }
    public Call<Demande> updateStatus(Long id, String statut) { return api.updateDemandeStatus(id, statut, true); }
}
