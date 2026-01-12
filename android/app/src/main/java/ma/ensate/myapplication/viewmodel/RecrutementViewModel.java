package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.model.Recrutement;
import ma.ensate.myapplication.model.RecrutementStats;
import ma.ensate.myapplication.repository.RecrutementRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecrutementViewModel extends ViewModel {
    private final RecrutementRepository repository = new RecrutementRepository();
    private final MutableLiveData<List<Recrutement>> recrutements = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<RecrutementStats> stats = new MutableLiveData<>();

    public LiveData<List<Recrutement>> getRecrutements() { return recrutements; }
    public LiveData<RecrutementStats> getStats() { return stats; }

    public void loadRecrutements() {
        repository.getRecrutements().enqueue(new Callback<List<Recrutement>>() {
            @Override
            public void onResponse(Call<List<Recrutement>> call, Response<List<Recrutement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recrutements.postValue(response.body());
                } else {
                    recrutements.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Recrutement>> call, Throwable t) {
                recrutements.postValue(new ArrayList<>());
            }
        });
    }

    public void loadStats() {
        repository.getStats().enqueue(new Callback<RecrutementStats>() {
            @Override
            public void onResponse(Call<RecrutementStats> call, Response<RecrutementStats> response) {
                if (response.isSuccessful()) {
                    stats.postValue(response.body());
                } else {
                    stats.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<RecrutementStats> call, Throwable t) {
                stats.postValue(null);
            }
        });
    }

    public interface ActionCallback { void onSuccess(Recrutement created); void onError(Throwable t); }

    public void createRecrutement(Recrutement r, ActionCallback cb) {
        repository.createRecrutement(r).enqueue(new Callback<Recrutement>() {
            @Override
            public void onResponse(Call<Recrutement> call, Response<Recrutement> response) {
                loadRecrutements();
                if (response.isSuccessful() && response.body() != null) cb.onSuccess(response.body()); else cb.onError(new Exception("Create failed"));
            }

            @Override
            public void onFailure(Call<Recrutement> call, Throwable t) {
                cb.onError(t);
            }
        });
    }
}
