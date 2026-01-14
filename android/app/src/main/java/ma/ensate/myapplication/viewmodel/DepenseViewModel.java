package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.repository.DepenseRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DepenseViewModel extends ViewModel {
    public final DepenseRepository repository = new DepenseRepository();
    private final MutableLiveData<List<Depense>> depenses = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Depense>> getDepenses() { return depenses; }

    public void loadDepenses() {
        repository.getDepenses().enqueue(new Callback<List<Depense>>() {
            @Override
            public void onResponse(Call<List<Depense>> call, Response<List<Depense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    depenses.postValue(response.body());
                } else {
                    depenses.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Depense>> call, Throwable t) {
                depenses.postValue(new ArrayList<>());
            }
        });
    }

    public interface ActionCallback { void onSuccess(Depense created); void onError(Throwable t); }

    public void createDepense(Depense d, ActionCallback callback) {
        repository.createDepense(d).enqueue(new Callback<Depense>() {
            @Override
            public void onResponse(Call<Depense> call, Response<Depense> response) {
                loadDepenses();
                if (response.isSuccessful() && response.body() != null) callback.onSuccess(response.body()); else callback.onError(new Exception("Create failed"));
            }

            @Override
            public void onFailure(Call<Depense> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}