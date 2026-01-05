package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

import java.util.List;

public class CommandeRepository {
    private final ApiService api;

    public CommandeRepository() {
        this.api = RetrofitClient.api();
    }

    public Call<List<Commande>> getCommandes() { return api.getCommandes(); }
    public Call<Commande> getCommande(Long id) { return api.getCommande(id); }
    public Call<Commande> createCommande(Commande c) { return api.createCommande(c); }
    public Call<Commande> updateCommande(Long id, Commande c) { return api.updateCommande(id, c); }
    public Call<List<Commande>> getByBesoin(Long besoinId) { return api.getCommandesByBesoin(besoinId); }
}