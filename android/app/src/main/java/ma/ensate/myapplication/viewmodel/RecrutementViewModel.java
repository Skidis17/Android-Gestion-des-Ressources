package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.model.Recrutement;
import ma.ensate.myapplication.repository.RecrutementRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecrutementViewModel extends ViewModel {
    private final RecrutementRepository repository = new RecrutementRepository();
    private final MutableLiveData<List<Recrutement>> recrutements = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Recrutement>> getRecrutements() { return recrutements; }

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
}
