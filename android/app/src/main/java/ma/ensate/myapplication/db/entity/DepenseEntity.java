package ma.ensate.myapplication.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "depenses")
public class DepenseEntity {
    @PrimaryKey(autoGenerate = true)
    private long localId;
    private Long serverId;
    private Long besoinId;
    private String categorie;
    private String montant;
    private String dateDepense;
    private String fournisseur;
    private String factureNumero;
    private String description;
    private String modePaiement;
    private Long enregistrePar;
    private String createdAt;
    private int syncStatus;

    public long getLocalId() { return localId; }
    public void setLocalId(long localId) { this.localId = localId; }
    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }
    public Long getBesoinId() { return besoinId; }
    public void setBesoinId(Long besoinId) { this.besoinId = besoinId; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getMontant() { return montant; }
    public void setMontant(String montant) { this.montant = montant; }
    public String getDateDepense() { return dateDepense; }
    public void setDateDepense(String dateDepense) { this.dateDepense = dateDepense; }
    public String getFournisseur() { return fournisseur; }
    public void setFournisseur(String fournisseur) { this.fournisseur = fournisseur; }
    public String getFactureNumero() { return factureNumero; }
    public void setFactureNumero(String factureNumero) { this.factureNumero = factureNumero; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public Long getEnregistrePar() { return enregistrePar; }
    public void setEnregistrePar(Long enregistrePar) { this.enregistrePar = enregistrePar; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }
}
