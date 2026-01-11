package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.Recette;
import ma.ensate.myapplication.repository.RecetteRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class RecetteViewModel extends ViewModel {
    private final RecetteRepository repository = new RecetteRepository();
    private final MutableLiveData<List<Recette>> recettes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<List<Recette>> getRecettes() { return recettes; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void loadRecettes() {
        loading.postValue(true);
        repository.getRecettes().enqueue(new Callback<List<Recette>>() {
            @Override
            public void onResponse(Call<List<Recette>> call, Response<List<Recette>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) recettes.postValue(response.body()); else recettes.postValue(new ArrayList<>());
            }

            @Override
            public void onFailure(Call<List<Recette>> call, Throwable t) {
                loading.postValue(false);
                recettes.postValue(new ArrayList<>());
            }
        });
    }

    public interface ActionCallback { void onSuccess(Recette created); void onError(Throwable t); }

    public void createRecette(Recette r, ActionCallback cb) {
        loading.postValue(true);
        repository.createRecette(r).enqueue(new Callback<Recette>() {
            @Override
            public void onResponse(Call<Recette> call, Response<Recette> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    loadRecettes();
                    cb.onSuccess(response.body());
                } else cb.onError(new Exception("Create failed"));
            }

            @Override
            public void onFailure(Call<Recette> call, Throwable t) {
                loading.postValue(false);
                cb.onError(t);
            }
        });
    }

    public void updateRecette(Recette r, ActionCallback cb) {
        loading.postValue(true);
        repository.updateRecette(r.getId(), r).enqueue(new Callback<Recette>() {
            @Override
            public void onResponse(Call<Recette> call, Response<Recette> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    loadRecettes();
                    cb.onSuccess(response.body());
                } else cb.onError(new Exception("Update failed"));
            }

            @Override
            public void onFailure(Call<Recette> call, Throwable t) {
                loading.postValue(false);
                cb.onError(t);
            }
        });
    }
}