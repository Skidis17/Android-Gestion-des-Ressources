package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.Recette;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

import java.util.List;

public class RecetteRepository {
    private final ApiService api;

    public RecetteRepository() {
        this.api = RetrofitClient.api();
    }

    public Call<List<Recette>> getRecettes() {
        return api.getRecettes();
    }

    public Call<Recette> createRecette(Recette r) {
        return api.createRecette(r);
    }

    public Call<Recette> updateRecette(Long id, Recette r) {
        return api.updateRecette(id, r);
    }

    public Call<Void> deleteRecette(Long id) {
        return api.deleteRecette(id);
    }
}
