package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.LoginRequest;
import ma.ensate.myapplication.model.LoginResponse;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

public class AuthRepository {

    private final ApiService api;

    public AuthRepository() {
        this.api = RetrofitClient.api();
    }

    public Call<LoginResponse> login(String email, String password) {
        return api.login(new LoginRequest(email, password));
    }
}
