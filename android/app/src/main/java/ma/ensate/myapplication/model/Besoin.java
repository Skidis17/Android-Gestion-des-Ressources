package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Besoin {
    private Long id;
    @SerializedName("personnelId")
    private Long personnelId;
    @SerializedName("typeBesoin")
    private String typeBesoin;
    private String description;
    private Integer quantite;
    private BigDecimal montantEstime;
    private String priorite;
    private String statut;
    private String commentaireAdmin;
    private Long traitePar;
    private String dateDemande; // use ISO strings for simplicity
    private String dateTraitement;
    private String dateLivraison;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPersonnelId() { return personnelId; }
    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }
    public String getTypeBesoin() { return typeBesoin; }
    public void setTypeBesoin(String typeBesoin) { this.typeBesoin = typeBesoin; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public BigDecimal getMontantEstime() { return montantEstime; }
    public void setMontantEstime(BigDecimal montantEstime) { this.montantEstime = montantEstime; }
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
}