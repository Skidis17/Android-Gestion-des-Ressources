package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.LoginRequest;
import ma.ensate.myapplication.model.LoginResponse;
import ma.ensate.myapplication.model.PasswordChangeRequest;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

public class AuthRepository {

    public Call<LoginResponse> login(String email, String password) {
        return RetrofitClient.api()
                .login(new LoginRequest(email, password));
    }

    public Call<String> changePassword(Long id, PasswordChangeRequest request) {
        return RetrofitClient.api().changePassword(id, request);
    }
}
