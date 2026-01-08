package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.LoginResponse;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.repository.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final AuthRepository repository = new AuthRepository();

    private final MutableLiveData<String> message = new MutableLiveData<>();
    public LiveData<String> getMessage() { return message; }

    public void login(String email, String password) {
        repository.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    String role = response.body().getRole();

                    // ✅ sauvegarder token + role
                    if (RetrofitClient.token() != null) {
                        RetrofitClient.token().saveAuth(token, role);
                    }

                    message.postValue("Vous êtes connecté avec le rôle " + role);
                } else {
                    message.postValue("Login échoué (code " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                message.postValue("Erreur réseau: " + t.getMessage());
            }
        });
    }
}
