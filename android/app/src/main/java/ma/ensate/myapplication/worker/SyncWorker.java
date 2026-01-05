package ma.ensate.myapplication.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import ma.ensate.myapplication.db.AppDatabase;
import ma.ensate.myapplication.db.dao.CommandeDao;
import ma.ensate.myapplication.db.entity.CommandeEntity;
import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Response;

public class SyncWorker extends Worker {
    public static final String TAG = "SyncWorker";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context ctx = getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(ctx);
        CommandeDao commandeDao = db.commandeDao();
        ApiService api = RetrofitClient.api();

        try {
            // Sync commandes pending create
            List<CommandeEntity> pendingCommandes = commandeDao.getPending();
            for (CommandeEntity e : pendingCommandes) {
                if (e.getSyncStatus() == 1) { // pending create
                    Commande payload = new Commande();
                    payload.setBesoinId(e.getBesoinId());
                    try { payload.setMontantTotal(e.getMontantTotal() == null ? null : new BigDecimal(e.getMontantTotal())); } catch (Exception ex) { payload.setMontantTotal(null); }
                    payload.setFournisseur(e.getFournisseur());
                    payload.setDateCommande(e.getDateCommande());
                    payload.setNotes(e.getNotes());

                    Call<Commande> call = api.createCommande(payload);
                    Response<Commande> resp = call.execute();
                    if (resp.isSuccessful() && resp.body() != null) {
                        e.setServerId(resp.body().getId());
                        e.setSyncStatus(0);
                        commandeDao.update(e);
                    }
                }
            }

            // Sync besoins pending create
            ma.ensate.myapplication.db.dao.BesoinDao besoinDao = db.besoinDao();
            List<ma.ensate.myapplication.db.entity.BesoinEntity> pendingBesoins = besoinDao.getPending();
            for (ma.ensate.myapplication.db.entity.BesoinEntity e : pendingBesoins) {
                if (e.getSyncStatus() == 1) {
                    ma.ensate.myapplication.model.Besoin payload = new ma.ensate.myapplication.model.Besoin();
                    // Ensure personnelId is always set (required field)
                    Long personnelId = e.getPersonnelId();
                    if (personnelId == null || personnelId <= 0) {
                        personnelId = 1L; // Default fallback
                        Log.w(TAG, "BesoinEntity " + e.getLocalId() + " had null/invalid personnelId, using default 1L");
                    }
                    payload.setPersonnelId(personnelId);
                    payload.setTypeBesoin(e.getTypeBesoin() != null ? e.getTypeBesoin().trim() : null);
                    payload.setDescription(e.getDescription() != null ? e.getDescription().trim() : null);
                    payload.setQuantite(e.getQuantite());
                    // montant
                    try { payload.setMontantEstime(e.getMontantEstime() == null ? null : new BigDecimal(e.getMontantEstime())); } catch (Exception ex) { payload.setMontantEstime(null); }
                    payload.setPriorite(e.getPriorite() != null ? e.getPriorite().trim() : null);
                    payload.setStatut(e.getStatut());
                    payload.setCommentaireAdmin(e.getCommentaireAdmin());
                    payload.setTraitePar(e.getTraitePar());
                    payload.setDateDemande(e.getDateDemande());
                    payload.setDateTraitement(e.getDateTraitement());
                    // Trim dateLivraison if present
                    String dateLiv = e.getDateLivraison();
                    if (dateLiv != null && dateLiv.length() >= 10) {
                        payload.setDateLivraison(dateLiv.substring(0, Math.min(10, dateLiv.length())));
                    } else {
                        payload.setDateLivraison(null);
                    }

                    Call<ma.ensate.myapplication.model.Besoin> callB = api.createBesoin(payload);
                    Response<ma.ensate.myapplication.model.Besoin> respB = callB.execute();
                    if (respB.isSuccessful() && respB.body() != null) {
                        e.setServerId(respB.body().getId());
                        e.setSyncStatus(0);
                        besoinDao.update(e);
                    }
                }
            }

            // Sync depenses pending create
            ma.ensate.myapplication.db.dao.DepenseDao depenseDao = db.depenseDao();
            List<ma.ensate.myapplication.db.entity.DepenseEntity> pendingDepenses = depenseDao.getPending();
            for (ma.ensate.myapplication.db.entity.DepenseEntity e : pendingDepenses) {
                if (e.getSyncStatus() == 1) {
                    ma.ensate.myapplication.model.Depense payload = new ma.ensate.myapplication.model.Depense();
                    payload.setBesoinId(e.getBesoinId());
                    try { payload.setMontant(e.getMontant() == null ? null : new BigDecimal(e.getMontant())); } catch (Exception ex) { payload.setMontant(null); }
                    payload.setCategorie(e.getCategorie());
                    payload.setDateDepense(e.getDateDepense());
                    payload.setFournisseur(e.getFournisseur());
                    payload.setFactureNumero(e.getFactureNumero());
                    payload.setDescription(e.getDescription());
                    payload.setModePaiement(e.getModePaiement());
                    payload.setEnregistrePar(e.getEnregistrePar());
                    payload.setCreatedAt(e.getCreatedAt());

                    Call<ma.ensate.myapplication.model.Depense> callD = api.createDepense(payload);
                    Response<ma.ensate.myapplication.model.Depense> respD = callD.execute();
                    if (respD.isSuccessful() && respD.body() != null) {
                        e.setServerId(respD.body().getId());
                        e.setSyncStatus(0);
                        depenseDao.update(e);
                    }
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "Network error during sync", ex);
            return Result.retry();
        } catch (Exception ex) {
            Log.e(TAG, "Error during sync", ex);
        }

        return Result.success();
    }
}
