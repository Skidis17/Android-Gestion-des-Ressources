package ma.ensate.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ma.ensate.myapplication.model.AddUserRequest;
import ma.ensate.myapplication.model.AddUserResponse;
import ma.ensate.myapplication.model.PersonnelOption;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserViewModel extends AndroidViewModel {

    private final ApiService api;

    private final MutableLiveData<List<PersonnelOption>> personnels = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> userAdded = new MutableLiveData<>(false);

    public AddUserViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.api(); // ✅ pas de context chez toi
    }

    public LiveData<List<PersonnelOption>> getPersonnels() { return personnels; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getUserAdded() { return userAdded; }

    // ✅ "Bearer <token>" ou null
    private String authHeader() {
        String token = new TokenManager(getApplication()).getToken();
        if (token == null || token.trim().isEmpty()) return null;
        return "Bearer " + token;
    }

    public void loadPersonnels() {
        String auth = authHeader();

        api.getAllPersonnels(auth).enqueue(new Callback<List<PersonnelOption>>() {
            @Override
            public void onResponse(Call<List<PersonnelOption>> call, Response<List<PersonnelOption>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    personnels.setValue(response.body());
                } else {
                    error.setValue("Erreur chargement personnel (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<PersonnelOption>> call, Throwable t) {
                error.setValue("Erreur réseau personnel");
            }
        });
    }

    public void addUser(AddUserRequest req) {
        userAdded.setValue(false);

        String auth = authHeader();
        if (auth == null) {
            error.setValue("Token manquant. Connecte-toi d'abord.");
            return;
        }

        api.addUser(auth, req).enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {
                if (response.isSuccessful()) {
                    userAdded.setValue(true);
                } else {
                    error.setValue("Erreur ajout user (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<AddUserResponse> call, Throwable t) {
                error.setValue("Erreur réseau addUser");
            }
        });
    }
}
