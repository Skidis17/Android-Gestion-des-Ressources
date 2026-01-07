package ma.ensate.myapplication.model;

import java.math.BigDecimal;

public class CandidatureRecrutement {
    private Long id;
    private Long recrutementId;
    private String nom;
    private String prenom;
    private String cin;
    private String email;
    private String telephone;
    private String cvUrl;
    private String lettreMotivationUrl;
    private String statut;
    private BigDecimal scoreEcrit;
    private BigDecimal scoreOral;
    private String commentaires;
    private String dateCandidature; // ISO string

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecrutementId() { return recrutementId; }
    public void setRecrutementId(Long recrutementId) { this.recrutementId = recrutementId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getCvUrl() { return cvUrl; }
    public void setCvUrl(String cvUrl) { this.cvUrl = cvUrl; }
    public String getLettreMotivationUrl() { return lettreMotivationUrl; }
    public void setLettreMotivationUrl(String lettreMotivationUrl) { this.lettreMotivationUrl = lettreMotivationUrl; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public BigDecimal getScoreEcrit() { return scoreEcrit; }
    public void setScoreEcrit(BigDecimal scoreEcrit) { this.scoreEcrit = scoreEcrit; }
    public BigDecimal getScoreOral() { return scoreOral; }
    public void setScoreOral(BigDecimal scoreOral) { this.scoreOral = scoreOral; }
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    public String getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(String dateCandidature) { this.dateCandidature = dateCandidature; }
}
