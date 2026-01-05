package ma.ensate.myapplication.repository.local;

import android.content.Context;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executors;

import ma.ensate.myapplication.db.AppDatabase;
import ma.ensate.myapplication.db.dao.CommandeDao;
import ma.ensate.myapplication.db.entity.CommandeEntity;
import ma.ensate.myapplication.model.Commande;

public class CommandeLocalRepository {
    private final CommandeDao dao;

    public CommandeLocalRepository(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        dao = db.commandeDao();
    }

    public void insertPendingFromModel(Commande c) {
        Executors.newSingleThreadExecutor().execute(() -> {
            CommandeEntity e = new CommandeEntity();
            e.setServerId(c.getId());
            e.setBesoinId(c.getBesoinId());
            e.setFournisseur(c.getFournisseur());
            e.setMontantTotal(c.getMontantTotal() != null ? c.getMontantTotal().toPlainString() : null);
            e.setDateCommande(c.getDateCommande());
            e.setNotes(c.getNotes());
            e.setSyncStatus(1); // pending create
            dao.insert(e);
        });
    }

    public void insertPendingFromFields(Long besoinId, String fournisseur, String montant, String dateCommande, String notes) {
        Executors.newSingleThreadExecutor().execute(() -> {
            CommandeEntity e = new CommandeEntity();
            e.setServerId(null);
            e.setBesoinId(besoinId);
            e.setFournisseur(fournisseur);
            e.setMontantTotal(montant);
            e.setDateCommande(dateCommande);
            e.setNotes(notes);
            e.setSyncStatus(1); // pending create
            dao.insert(e);
        });
    }

    public void markSyncedWithServer(Commande created) {
        Executors.newSingleThreadExecutor().execute(() -> {
            String montant = created.getMontantTotal() != null ? created.getMontantTotal().toPlainString() : null;
            CommandeEntity pending = dao.findPendingMatching(created.getFournisseur(), montant, created.getDateCommande());
            if (pending != null) {
                pending.setServerId(created.getId());
                pending.setSyncStatus(0);
                dao.update(pending);
            }
        });
    }

    public List<CommandeEntity> getPendingNow() {
        return dao.getPending();
    }
}
