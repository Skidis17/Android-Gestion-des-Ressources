package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

import java.util.List;

public class DepenseRepository {
    private final ApiService api;

    public DepenseRepository() {
        this.api = RetrofitClient.api();
    }

    public Call<List<Depense>> getDepenses() { return api.getDepenses(); }
    public Call<Depense> getDepense(Long id) { return api.getDepense(id); }
    public Call<Depense> createDepense(Depense d) { return api.createDepense(d); }
    public Call<Depense> updateDepense(Long id, Depense d) { return api.updateDepense(id, d); }
    public Call<List<Depense>> getByBesoin(Long besoinId) { return api.getDepensesByBesoin(besoinId); }
}