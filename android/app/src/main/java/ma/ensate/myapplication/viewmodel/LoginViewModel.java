package ma.ensate.myapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ma.ensate.myapplication.model.LoginResponse;
import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.repository.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LOGIN";

    private final AuthRepository repo = new AuthRepository();

    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<String> roleLiveData = new MutableLiveData<>();

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<String> getRole() {
        return roleLiveData;
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String password) {
        Log.d(TAG, "login() called email=" + email);

        repo.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.d(TAG, "HTTP CODE=" + response.code() + " MSG=" + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    String role  = response.body().getRole();

                    Log.d(TAG, "SUCCESS token=" + token + " role=" + role);

                    new TokenManager(getApplication()).saveAuth(token, role);

                    roleLiveData.postValue(role);

                    message.postValue("Vous êtes connecté (" + role + ")");
                } else {
                    String err = "";
                    try {
                        if (response.errorBody() != null) err = response.errorBody().string();
                    } catch (Exception ignored) {}

                    Log.e(TAG, "LOGIN FAILED body=" + err);
                    message.postValue("Login échoué (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "NETWORK ERROR", t);
                message.postValue("Erreur réseau : " + t.getMessage());
            }
        });
    }
}
