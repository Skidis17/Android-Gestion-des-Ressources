package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.repository.CandidatureRecrutementRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatureRecrutementViewModel extends ViewModel {
    private final CandidatureRecrutementRepository repository = new CandidatureRecrutementRepository();
    private final MutableLiveData<List<CandidatureRecrutement>> candidatures = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<CandidatureRecrutement> selected = new MutableLiveData<>();

    public LiveData<List<CandidatureRecrutement>> getCandidatures() { return candidatures; }
    public LiveData<CandidatureRecrutement> getSelected() { return selected; }

    public void loadByRecrutement(Long recrutementId) {
        repository.getByRecrutement(recrutementId).enqueue(new Callback<List<CandidatureRecrutement>>() {
            @Override
            public void onResponse(Call<List<CandidatureRecrutement>> call, Response<List<CandidatureRecrutement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    candidatures.postValue(response.body());
                } else {
                    candidatures.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CandidatureRecrutement>> call, Throwable t) {
                candidatures.postValue(new ArrayList<>());
            }
        });
    }

    public interface ActionCallback { void onSuccess(CandidatureRecrutement c); void onError(Throwable t); }

    public void create(CandidatureRecrutement c, ActionCallback cb) {
        repository.create(c).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                loadByRecrutement(c.getRecrutementId());
                if (response.isSuccessful() && response.body() != null) cb.onSuccess(response.body()); else cb.onError(new Exception("Create failed"));
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void loadById(Long id) {
        repository.getById(id).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                if (response.isSuccessful()) selected.postValue(response.body());
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                selected.postValue(null);
            }
        });
    }

    public void updateStatus(Long id, String statut, boolean sendEmail, ActionCallback cb) {
        repository.updateStatus(id, statut, sendEmail).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selected.postValue(response.body());
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Update failed"));
                }
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void selectAccepted(Long recrutementId, boolean sendEmail, ListCallback cb) {
        repository.selectAccepted(recrutementId, sendEmail).enqueue(new Callback<List<CandidatureRecrutement>>() {
            @Override
            public void onResponse(Call<List<CandidatureRecrutement>> call, Response<List<CandidatureRecrutement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    candidatures.postValue(response.body());
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Selection failed"));
                }
            }

            @Override
            public void onFailure(Call<List<CandidatureRecrutement>> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public interface ListCallback { void onSuccess(List<CandidatureRecrutement> list); void onError(Throwable t); }
}
