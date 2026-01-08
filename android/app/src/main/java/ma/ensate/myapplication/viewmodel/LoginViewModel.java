package ma.ensate.myapplication.viewmodel;

import android.app.Application;

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

    private final AuthRepository repo = new AuthRepository();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<String> getMessage() {
        return message;
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String password) {
        repo.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                // 🔍 LOGS
                android.util.Log.d("LOGIN", "HTTP CODE = " + response.code());
                android.util.Log.d("LOGIN", "MESSAGE = " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    String role = response.body().getRole();
                    message.postValue("Vous êtes connecté avec le rôle " + role);
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    android.util.Log.e("LOGIN", "ERROR BODY = " + errorBody);
                    message.postValue("Login échoué (code " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                android.util.Log.e("LOGIN", "FAILURE", t);
                message.postValue("Erreur réseau : " + t.getMessage());
            }
        });
    }



}

