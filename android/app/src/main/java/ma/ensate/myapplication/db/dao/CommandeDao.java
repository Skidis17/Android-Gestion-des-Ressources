package ma.ensate.myapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensate.myapplication.db.entity.CommandeEntity;

@Dao
public interface CommandeDao {
    @Insert
    long insert(CommandeEntity c);

    @Update
    void update(CommandeEntity c);

    @Delete
    void delete(CommandeEntity c);

    @Query("SELECT * FROM commandes ORDER BY localId DESC")
    List<CommandeEntity> getAll();

    @Query("SELECT * FROM commandes WHERE syncStatus != 0")
    List<CommandeEntity> getPending();

    @Query("SELECT * FROM commandes WHERE serverId = :serverId LIMIT 1")
    CommandeEntity findByServerId(Long serverId);

    @Query("SELECT * FROM commandes WHERE syncStatus = 1 AND fournisseur = :fournisseur AND montantTotal = :montant AND dateCommande = :dateCommande LIMIT 1")
    CommandeEntity findPendingMatching(String fournisseur, String montant, String dateCommande);
}
