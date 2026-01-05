package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Depense {
    private Long id;
    @SerializedName("besoinId")
    private Long besoinId;
    private String categorie;
    private BigDecimal montant;
    private String dateDepense;
    private String fournisseur;
    private String factureNumero;
    private String description;
    private String modePaiement;
    private Long enregistrePar;
    private String createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBesoinId() { return besoinId; }
    public void setBesoinId(Long besoinId) { this.besoinId = besoinId; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
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
}