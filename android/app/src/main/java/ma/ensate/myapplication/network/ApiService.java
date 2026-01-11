package ma.ensate.myapplication.network;

import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.model.LoginRequest;
import ma.ensate.myapplication.model.LoginResponse;
import ma.ensate.myapplication.model.Recrutement;
import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.model.BudgetSummary;
import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.model.UploadResponse;
import retrofit2.Call;
import retrofit2.http.*;
import okhttp3.MultipartBody;

import java.util.List;
import ma.ensate.myapplication.model.Recette;

public interface ApiService {

    // Auth
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

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
    Call<Besoin> changeBesoinStatus(@Path("id") Long id, @Query("statut") String statut,
            @Query("traitePar") Long traitePar, @Query("commentaire") String commentaire);

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

    // Recrutements
    @GET("api/v1/recrutements")
    Call<List<Recrutement>> getRecrutements();

    @GET("api/v1/recrutements/{id}")
    Call<Recrutement> getRecrutement(@Path("id") Long id);

    @POST("api/v1/recrutements")
    Call<Recrutement> createRecrutement(@Body Recrutement recrutement);

    @PUT("api/v1/recrutements/{id}")
    Call<Recrutement> updateRecrutement(@Path("id") Long id, @Body Recrutement recrutement);

    @POST("api/v1/recrutements/{id}/status")
    Call<Recrutement> changeRecrutementStatus(@Path("id") Long id, @Query("statut") String statut);

    // Candidatures recrutement
    @GET("api/v1/candidatures-recrutement/by-recrutement/{recrutementId}")
    Call<List<CandidatureRecrutement>> getCandidaturesByRecrutement(@Path("recrutementId") Long recrutementId);

    @GET("api/v1/candidatures-recrutement/{id}")
    Call<CandidatureRecrutement> getCandidature(@Path("id") Long id);

    @POST("api/v1/candidatures-recrutement")
    Call<CandidatureRecrutement> createCandidature(@Body CandidatureRecrutement candidature);

    @POST("api/v1/candidatures-recrutement/{id}/status")
    Call<CandidatureRecrutement> updateCandidatureStatus(@Path("id") Long id,
            @Query("statut") String statut,
            @Query("sendEmail") boolean sendEmail);

    @POST("api/v1/recrutements/{id}/select")
    Call<List<CandidatureRecrutement>> selectAcceptedCandidates(@Path("id") Long recrutementId,
            @Query("sendEmail") boolean sendEmail);

    @Multipart
    @POST("api/v1/uploads")
    Call<UploadResponse> uploadFile(@Part MultipartBody.Part file);
}
