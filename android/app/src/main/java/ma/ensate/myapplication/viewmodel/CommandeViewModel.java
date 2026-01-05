package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.repository.CommandeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class CommandeViewModel extends ViewModel {
    private final CommandeRepository repository = new CommandeRepository();
    private final MutableLiveData<List<Commande>> commandes = new MutableLiveData<>(new ArrayList<>());
    public interface ActionCallback { void onSuccess(Commande created); void onError(Throwable t); }

    public LiveData<List<Commande>> getCommandes() { return commandes; }

    public void loadCommandes() {
        repository.getCommandes().enqueue(new Callback<List<Commande>>() {
            @Override
            public void onResponse(Call<List<Commande>> call, Response<List<Commande>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commandes.postValue(response.body());
                } else {
                    commandes.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Commande>> call, Throwable t) {
                commandes.postValue(new ArrayList<>());
            }
        });
    }

    public void createCommande(Commande c, ActionCallback callback) {
        repository.createCommande(c).enqueue(new Callback<Commande>() {
            @Override
            public void onResponse(Call<Commande> call, Response<Commande> response) {
                loadCommandes();
                if (response.isSuccessful() && response.body() != null) callback.onSuccess(response.body()); else callback.onError(new Exception("Create failed"));
            }

            @Override
            public void onFailure(Call<Commande> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}