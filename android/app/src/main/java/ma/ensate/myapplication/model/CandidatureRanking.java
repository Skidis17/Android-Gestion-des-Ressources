package ma.ensate.myapplication.model;

import java.math.BigDecimal;

public class CandidatureRanking {
    private Long candidatureId;
    private String nom;
    private String prenom;
    private String email;
    private String statut;
    private BigDecimal scoreEcrit;
    private BigDecimal scoreOral;
    private BigDecimal interviewScore;
    private BigDecimal totalScore;

    public Long getCandidatureId() { return candidatureId; }
    public void setCandidatureId(Long candidatureId) { this.candidatureId = candidatureId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public BigDecimal getScoreEcrit() { return scoreEcrit; }
    public void setScoreEcrit(BigDecimal scoreEcrit) { this.scoreEcrit = scoreEcrit; }
    public BigDecimal getScoreOral() { return scoreOral; }
    public void setScoreOral(BigDecimal scoreOral) { this.scoreOral = scoreOral; }
    public BigDecimal getInterviewScore() { return interviewScore; }
    public void setInterviewScore(BigDecimal interviewScore) { this.interviewScore = interviewScore; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
}
