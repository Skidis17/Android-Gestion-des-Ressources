package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.Besoin;
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

    public Call<Besoin> changeStatus(Long id, String statut, Long traitePar, String commentaire) {
        return api.changeBesoinStatus(id, statut, traitePar, commentaire);
    }

    public retrofit2.Call<Void> deleteBesoin(Long id) {
        return api.deleteBesoin(id);
    }
}