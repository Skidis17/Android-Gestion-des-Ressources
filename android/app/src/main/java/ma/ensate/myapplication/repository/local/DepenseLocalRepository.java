package ma.ensate.myapplication.repository.local;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executors;

import ma.ensate.myapplication.db.AppDatabase;
import ma.ensate.myapplication.db.dao.DepenseDao;
import ma.ensate.myapplication.db.entity.DepenseEntity;
import ma.ensate.myapplication.model.Depense;

public class DepenseLocalRepository {
    private final DepenseDao dao;

    public DepenseLocalRepository(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        dao = db.depenseDao();
    }

    public void insertPendingFromModel(Depense d) {
        Executors.newSingleThreadExecutor().execute(() -> {
            DepenseEntity e = new DepenseEntity();
            e.setServerId(d.getId());
            e.setBesoinId(d.getBesoinId());
            e.setCategorie(d.getCategorie());
            e.setMontant(d.getMontant() != null ? d.getMontant().toPlainString() : null);
            e.setDateDepense(d.getDateDepense());
            e.setFournisseur(d.getFournisseur());
            e.setFactureNumero(d.getFactureNumero());
            e.setDescription(d.getDescription());
            e.setModePaiement(d.getModePaiement());
            e.setEnregistrePar(d.getEnregistrePar());
            e.setCreatedAt(d.getCreatedAt());
            e.setSyncStatus(1);
            dao.insert(e);
        });
    }

    public List<DepenseEntity> getPendingNow() {
        return dao.getPending();
    }

    public void markSyncedWithServer(Depense created) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<DepenseEntity> pending = dao.getPending();
            for (DepenseEntity e : pending) {
                if (e.getFournisseur() != null && e.getFournisseur().equals(created.getFournisseur()) && e.getMontant() != null && e.getMontant().equals(created.getMontant() != null ? created.getMontant().toPlainString() : null)) {
                    e.setServerId(created.getId());
                    e.setSyncStatus(0);
                    dao.update(e);
                    break;
                }
            }
        });
    }
}
