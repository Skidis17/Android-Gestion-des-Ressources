package ma.ensate.myapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ma.ensate.myapplication.model.PasswordChangeRequest;
import ma.ensate.myapplication.model.UpdateProfileRequest;
import ma.ensate.myapplication.repository.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = "PROFILE_VM";
    private final AuthRepository repo = new AuthRepository();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<String> getMessage() {
        return message;
    }

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void changePassword(Long userId, String oldPass, String newPass) {
        Log.d(TAG, "changePassword userId=" + userId);

        repo.changePassword(userId, new PasswordChangeRequest(oldPass, newPass))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "HTTP=" + response.code());
                        if (response.isSuccessful()) {
                            message.postValue(response.body() != null ? response.body() : "Succès");
                        } else {
                            try {
                                message.postValue(response.errorBody() != null ? response.errorBody().string() : "Erreur");
                            } catch (Exception e) {
                                message.postValue("Erreur inconnue");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "NETWORK ERROR", t);
                        message.postValue("Erreur réseau: " + t.getMessage());
                    }
                });
    }

    public void updateProfile(Long userId, String username) {
        Log.d(TAG, "updateProfile userId=" + userId + " username=" + username);

        repo.updateProfile(userId, new UpdateProfileRequest(username))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "HTTP=" + response.code());
                        if (response.isSuccessful()) {
                            message.postValue(response.body() != null ? response.body() : "Profil mis à jour");
                        } else {
                            try {
                                message.postValue(response.errorBody() != null ? response.errorBody().string() : "Erreur");
                            } catch (Exception e) {
                                message.postValue("Erreur inconnue");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "NETWORK ERROR", t);
                        message.postValue("Erreur réseau: " + t.getMessage());
                    }
                });
    }
}
