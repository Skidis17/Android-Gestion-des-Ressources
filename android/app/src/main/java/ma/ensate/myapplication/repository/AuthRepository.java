package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.LoginRequest;
import ma.ensate.myapplication.model.LoginResponse;
import ma.ensate.myapplication.network.ApiService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {

    private final ApiService api;

    public AuthRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")  // ✅ émulateur
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

    public Call<LoginResponse> login(String email, String password) {
        return api.login(new LoginRequest(email, password));
    }
}
