package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.repository.BesoinRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class BesoinViewModel extends ViewModel {
    public final BesoinRepository repository = new BesoinRepository();
    private final MutableLiveData<List<Besoin>> besoins = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> actionResult = new MutableLiveData<>();

    public LiveData<List<Besoin>> getBesoins() { return besoins; }
    public LiveData<Boolean> getActionResult() { return actionResult; }

    public void loadBesoins() {
        repository.getBesoins().enqueue(new Callback<List<Besoin>>() {
            @Override
            public void onResponse(Call<List<Besoin>> call, Response<List<Besoin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    besoins.postValue(response.body());
                } else {
                    besoins.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Besoin>> call, Throwable t) {
                besoins.postValue(new ArrayList<>());
            }
        });
    }

    // callback interface for fragments
    public interface ActionCallback { void onSuccess(Besoin created); void onError(Throwable t); }

    public void createBesoin(Besoin b, ActionCallback callback) {
        repository.createBesoin(b).enqueue(new Callback<Besoin>() {
            @Override
            public void onResponse(Call<Besoin> call, Response<Besoin> response) {
                loadBesoins();
                actionResult.postValue(response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Create failed";
                    if (response.code() == 400) {
                        errorMsg = "Validation error: Check required fields";
                    } else if (response.code() == 500) {
                        errorMsg = "Server error";
                    } else {
                        errorMsg = "Error " + response.code() + ": " + response.message();
                    }
                    callback.onError(new Exception(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<Besoin> call, Throwable t) {
                actionResult.postValue(false);
                callback.onError(t);
            }
        });
    }

    public void updateBesoin(Long id, Besoin b, ActionCallback callback) {
        repository.updateBesoin(id, b).enqueue(new Callback<Besoin>() {
            @Override
            public void onResponse(Call<Besoin> call, Response<Besoin> response) {
                loadBesoins();
                actionResult.postValue(response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) callback.onSuccess(response.body()); else callback.onError(new Exception("Update failed"));
            }

            @Override
            public void onFailure(Call<Besoin> call, Throwable t) {
                actionResult.postValue(false);
                callback.onError(t);
            }
        });
    }

    public void deleteBesoin(Long id, ActionCallback callback) {
        repository.deleteBesoin(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadBesoins();
                actionResult.postValue(response.isSuccessful());
                if (response.isSuccessful()) {
                    // Create a dummy Besoin object for callback compatibility
                    Besoin deleted = new Besoin();
                    deleted.setId(id);
                    callback.onSuccess(deleted);
                } else {
                    callback.onError(new Exception("Delete failed"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                actionResult.postValue(false);
                callback.onError(t);
            }
        });
    }

    public void changeStatus(Long id, String statut, Long traitePar, String commentaire, ActionCallback callback) {
        repository.changeStatus(id, statut, traitePar, commentaire).enqueue(new Callback<Besoin>() {
            @Override
            public void onResponse(Call<Besoin> call, Response<Besoin> response) {
                loadBesoins();
                actionResult.postValue(response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Status change failed"));
                }
            }

            @Override
            public void onFailure(Call<Besoin> call, Throwable t) {
                actionResult.postValue(false);
                callback.onError(t);
            }
        });
    }
}