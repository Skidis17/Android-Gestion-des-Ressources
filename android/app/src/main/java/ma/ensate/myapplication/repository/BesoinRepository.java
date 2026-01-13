package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.model.StatusChangeRequest;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

import java.util.List;

public class BesoinRepository {
    private final ApiService api;

    public BesoinRepository() {
        this.api = RetrofitClient.api();
    }

    public Call<List<Besoin>> getBesoins() {
        return api.getBesoins();
    }

    public Call<Besoin> getBesoin(Long id) {
        return api.getBesoin(id);
    }

    public Call<Besoin> createBesoin(Besoin b) {
        return api.createBesoin(b);
    }

    public Call<Besoin> updateBesoin(Long id, Besoin b) {
        return api.updateBesoin(id, b);
    }

    public Call<Besoin> changeStatus(Long id, String statut, String commentaire) {
        StatusChangeRequest request = new StatusChangeRequest(statut, commentaire, false, null);
        return api.changeBesoinStatus(id, request);
    }

    public Call<ma.ensate.myapplication.model.Commande> createCommandeFromBesoin(Long besoinId, java.util.Map<String, String> request) {
        return api.createCommandeFromBesoin(besoinId, request);
    }

    public retrofit2.Call<Void> deleteBesoin(Long id) {
        return api.deleteBesoin(id);
    }
}