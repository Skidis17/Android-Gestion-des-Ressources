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

    public LiveData<String> getMessage() { return message; }
    public LiveData<String> getRole() { return roleLiveData; }

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String password) {
        repo.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse body = response.body();
                    Long id = body.getId(); // ✅ récupération ID
                    String token = body.getToken();
                    String role = body.getRole();
                    String username = body.getUsername();
                    String userEmail = body.getEmail();
                    Log.d(TAG, "BACKEND RESPONSE ↓↓↓");
                    Log.d(TAG, "id=" + id);
                    Log.d(TAG, "token=" + token);
                    Log.d(TAG, "role=" + role);
                    Log.d(TAG, "username=" + username);
                    Log.d(TAG, "email=" + userEmail);

                    if (userEmail == null || userEmail.isEmpty()) userEmail = email;
                    if (username == null) username = "";

                    new TokenManager(getApplication()).saveAuth(id,token, role, username, userEmail);

                    Log.d(TAG, "SAVED IN TOKEN MANAGER ↓↓↓");
                    Log.d(TAG, "id=" + id);
                    Log.d(TAG, "token=" + token);
                    Log.d(TAG, "role=" + role);
                    Log.d(TAG, "username=" + username);
                    Log.d(TAG, "email=" + userEmail);
                    roleLiveData.postValue(role);
                    message.postValue("Vous êtes connecté (" + role + ")");

                } else {
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
