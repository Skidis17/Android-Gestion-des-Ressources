package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Commande {
    private Long id;
    @SerializedName("besoinId")
    private Long besoinId;
    private String fournisseur;
    private BigDecimal montantTotal;
    private String dateCommande;
    private String dateLivraisonPrevue;
    private String dateLivraisonEffective;
    private String statut;
    private String bonCommandeNumero;
    private String notes;
    private Long createdBy;
    private String createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBesoinId() { return besoinId; }
    public void setBesoinId(Long besoinId) { this.besoinId = besoinId; }
    public String getFournisseur() { return fournisseur; }
    public void setFournisseur(String fournisseur) { this.fournisseur = fournisseur; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
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
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}