package ma.ensate.myapplication.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "besoins")
public class BesoinEntity {
    @PrimaryKey(autoGenerate = true)
    private long localId;
    private Long serverId;
    private Long personnelId;
    private String typeBesoin;
    private String description;
    private int quantite;
    private String montantEstime;
    private String priorite;
    private String statut;
    private String commentaireAdmin;
    private Long traitePar;
    private String dateDemande;
    private String dateTraitement;
    private String dateLivraison;
    private int syncStatus; // 0 = synced, 1 = pending create, etc.

    public long getLocalId() { return localId; }
    public void setLocalId(long localId) { this.localId = localId; }
    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }
    public Long getPersonnelId() { return personnelId; }
    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }
    public String getTypeBesoin() { return typeBesoin; }
    public void setTypeBesoin(String typeBesoin) { this.typeBesoin = typeBesoin; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public String getMontantEstime() { return montantEstime; }
    public void setMontantEstime(String montantEstime) { this.montantEstime = montantEstime; }
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getCommentaireAdmin() { return commentaireAdmin; }
    public void setCommentaireAdmin(String commentaireAdmin) { this.commentaireAdmin = commentaireAdmin; }
    public Long getTraitePar() { return traitePar; }
    public void setTraitePar(Long traitePar) { this.traitePar = traitePar; }
    public String getDateDemande() { return dateDemande; }
    public void setDateDemande(String dateDemande) { this.dateDemande = dateDemande; }
    public String getDateTraitement() { return dateTraitement; }
    public void setDateTraitement(String dateTraitement) { this.dateTraitement = dateTraitement; }
    public String getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(String dateLivraison) { this.dateLivraison = dateLivraison; }
    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }
}
