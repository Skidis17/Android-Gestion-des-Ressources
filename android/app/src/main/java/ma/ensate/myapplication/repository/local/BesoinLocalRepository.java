package ma.ensate.myapplication.repository.local;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executors;

import ma.ensate.myapplication.db.AppDatabase;
import ma.ensate.myapplication.db.dao.BesoinDao;
import ma.ensate.myapplication.db.entity.BesoinEntity;
import ma.ensate.myapplication.model.Besoin;

public class BesoinLocalRepository {
    private final BesoinDao dao;

    public BesoinLocalRepository(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        dao = db.besoinDao();
    }

    public void insertPendingFromModel(Besoin b) {
        Executors.newSingleThreadExecutor().execute(() -> {
            BesoinEntity e = new BesoinEntity();
            e.setServerId(b.getId());
            e.setPersonnelId(b.getPersonnelId());
            e.setTypeBesoin(b.getTypeBesoin());
            e.setDescription(b.getDescription());
            e.setQuantite(b.getQuantite());
            e.setMontantEstime(b.getMontantEstime() != null ? b.getMontantEstime().toPlainString() : null);
            e.setPriorite(b.getPriorite());
            e.setStatut(b.getStatut());
            e.setCommentaireAdmin(b.getCommentaireAdmin());
            e.setTraitePar(b.getTraitePar());
            e.setDateDemande(b.getDateDemande());
            e.setDateTraitement(b.getDateTraitement());
            e.setDateLivraison(b.getDateLivraison());
            e.setSyncStatus(1);
            dao.insert(e);
        });
    }

    public List<BesoinEntity> getPendingNow() {
        return dao.getPending();
    }

    public void markSyncedWithServer(Besoin created) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // naive: mark first pending as synced if matches personnelId and dateDemande
            List<BesoinEntity> pending = dao.getPending();
            for (BesoinEntity e : pending) {
                if (e.getPersonnelId() != null && e.getPersonnelId().equals(created.getPersonnelId()) && e.getDateDemande() != null && e.getDateDemande().equals(created.getDateDemande())) {
                    e.setServerId(created.getId());
                    e.setSyncStatus(0);
                    dao.update(e);
                    break;
                }
            }
        });
    }
}
