package ma.ensate.myapplication.network;

import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.model.BudgetSummary;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import ma.ensate.myapplication.model.Recette;

public interface ApiService {

    // Besoins
    @GET("api/v1/besoins")
    Call<List<Besoin>> getBesoins();

    @GET("api/v1/besoins/{id}")
    Call<Besoin> getBesoin(@Path("id") Long id);

    @POST("api/v1/besoins")
    Call<Besoin> createBesoin(@Body Besoin besoin);

    @PUT("api/v1/besoins/{id}")
    Call<Besoin> updateBesoin(@Path("id") Long id, @Body Besoin besoin);

    @POST("api/v1/besoins/{id}/status")
    Call<Besoin> changeBesoinStatus(@Path("id") Long id, @Query("statut") String statut, @Query("traitePar") Long traitePar, @Query("commentaire") String commentaire);

    @DELETE("api/v1/besoins/{id}")
    Call<Void> deleteBesoin(@Path("id") Long id);

    // Depenses
    @GET("api/v1/depenses")
    Call<List<Depense>> getDepenses();

    @GET("api/v1/depenses/{id}")
    Call<Depense> getDepense(@Path("id") Long id);

    @POST("api/v1/depenses")
    Call<Depense> createDepense(@Body Depense depense);

    @PUT("api/v1/depenses/{id}")
    Call<Depense> updateDepense(@Path("id") Long id, @Body Depense depense);

    @GET("api/v1/depenses/by-besoin/{besoinId}")
    Call<List<Depense>> getDepensesByBesoin(@Path("besoinId") Long besoinId);

    // Commandes
    @GET("api/v1/commandes")
    Call<List<Commande>> getCommandes();

    @GET("api/v1/commandes/{id}")
    Call<Commande> getCommande(@Path("id") Long id);

    @POST("api/v1/commandes")
    Call<Commande> createCommande(@Body Commande commande);

    @PUT("api/v1/commandes/{id}")
    Call<Commande> updateCommande(@Path("id") Long id, @Body Commande commande);

    @GET("api/v1/commandes/by-besoin/{besoinId}")
    Call<List<Commande>> getCommandesByBesoin(@Path("besoinId") Long besoinId);

    // Recettes
    @GET("api/v1/recettes")
    Call<List<Recette>> getRecettes();

    @POST("api/v1/recettes")
    Call<Recette> createRecette(@Body Recette recette);

    @PUT("api/v1/recettes/{id}")
    Call<Recette> updateRecette(@Path("id") Long id, @Body Recette recette);

    // Budget summary
    @GET("api/v1/budget/summary")
    Call<BudgetSummary> getBudgetSummary();
}