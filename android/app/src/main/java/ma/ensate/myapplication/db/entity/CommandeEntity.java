package ma.ensate.myapplication.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "commandes")
public class CommandeEntity {
    @PrimaryKey(autoGenerate = true)
    private long localId;

    private Long serverId; // id from backend
    private Long besoinId;
    private String fournisseur;
    private String montantTotal; // stored as string
    private String dateCommande;
    private String dateLivraisonPrevue;
    private String dateLivraisonEffective;
    private String statut;
    private String bonCommandeNumero;
    private String notes;
    private int syncStatus; // 0 = synced, 1 = pending create, 2 = pending update, 3 = pending delete
    private String createdAt;

    public long getLocalId() { return localId; }
    public void setLocalId(long localId) { this.localId = localId; }
    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }
    public Long getBesoinId() { return besoinId; }
    public void setBesoinId(Long besoinId) { this.besoinId = besoinId; }
    public String getFournisseur() { return fournisseur; }
    public void setFournisseur(String fournisseur) { this.fournisseur = fournisseur; }
    public String getMontantTotal() { return montantTotal; }
    public void setMontantTotal(String montantTotal) { this.montantTotal = montantTotal; }
    public String getDateCommande() { return dateCommande; }
    public void setDateCommande(String dateCommande) { this.dateCommande = dateCommande; }
    public String getDateLivraisonPrevue() { return dateLivraisonPrevue; }
    public void setDateLivraisonPrevue(String dateLivraisonPrevue) { this.dateLivraisonPrevue = dateLivraisonPrevue; }
    public String getDateLivraisonEffective() { return dateLivraisonEffective; }
    public void setDateLivraisonEffective(String dateLivraisonEffective) { this.dateLivraisonEffective = dateLivraisonEffective; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getBonCommandeNumero() { return bonCommandeNumero; }
    public void setBonCommandeNumero(String bonCommandeNumero) { this.bonCommandeNumero = bonCommandeNumero; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
