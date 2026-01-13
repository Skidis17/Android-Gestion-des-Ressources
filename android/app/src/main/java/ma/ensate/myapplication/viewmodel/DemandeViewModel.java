package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.model.Demande;
import ma.ensate.myapplication.repository.DemandeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemandeViewModel extends ViewModel {
    private final DemandeRepository repository = new DemandeRepository();
    private final MutableLiveData<List<Demande>> demandes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Demande>> allDemandesForStats = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Demande> selected = new MutableLiveData<>();

    public LiveData<List<Demande>> getDemandes() { return demandes; }
    public LiveData<List<Demande>> getAllDemandesForStats() { return allDemandesForStats; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<Demande> getSelected() { return selected; }

    public void loadDemandes(String statut) {
        loading.postValue(true);
        repository.getDemandes(statut).enqueue(new Callback<List<Demande>>() {
            @Override
            public void onResponse(Call<List<Demande>> call, Response<List<Demande>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    demandes.postValue(response.body());
                } else {
                    demandes.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Demande>> call, Throwable t) {
                loading.postValue(false);
                demandes.postValue(new ArrayList<>());
            }
        });
    }

    public void loadAllDemandesForStats() {
        repository.getDemandes(null).enqueue(new Callback<List<Demande>>() {
            @Override
            public void onResponse(Call<List<Demande>> call, Response<List<Demande>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allDemandesForStats.postValue(response.body());
                } else {
                    allDemandesForStats.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Demande>> call, Throwable t) {
                allDemandesForStats.postValue(new ArrayList<>());
            }
        });
    }

    public interface ActionCallback { void onSuccess(Demande created); void onError(Throwable t); }

    public void createDemande(Demande demande, ActionCallback cb) {
        loading.postValue(true);
        repository.createDemande(demande).enqueue(new Callback<Demande>() {
            @Override
            public void onResponse(Call<Demande> call, Response<Demande> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Create failed"));
                }
            }

            @Override
            public void onFailure(Call<Demande> call, Throwable t) {
                loading.postValue(false);
                cb.onError(t);
            }
        });
    }

    public void loadById(Long id) {
        repository.getDemande(id).enqueue(new Callback<Demande>() {
            @Override
            public void onResponse(Call<Demande> call, Response<Demande> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selected.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Demande> call, Throwable t) { }
        });
    }

    public void updateStatus(Long id, String statut, ActionCallback cb) {
        repository.updateStatus(id, statut).enqueue(new Callback<Demande>() {
            @Override
            public void onResponse(Call<Demande> call, Response<Demande> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selected.postValue(response.body());
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Update failed"));
                }
            }

            @Override
            public void onFailure(Call<Demande> call, Throwable t) {
                cb.onError(t);
            }
        });
    }
}
