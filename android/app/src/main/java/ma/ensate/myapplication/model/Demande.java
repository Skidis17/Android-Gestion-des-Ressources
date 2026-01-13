package ma.ensate.myapplication.model;

public class Demande {
    private Long id;
    private String type;
    private String dateDebut;
    private String dateFin;
    private String motif;
    private String justificatifUrl;
    private String statut;
    private Long createdBy;
    private String createdByName;
    private String createdAt;

    public Demande() {}

    public Demande(Long id, String type, String dateDebut, String dateFin, String motif,
                   String justificatifUrl, String statut, Long createdBy, String createdAt) {
        this.id = id;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.justificatifUrl = justificatifUrl;
        this.statut = statut;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getJustificatifUrl() { return justificatifUrl; }
    public void setJustificatifUrl(String justificatifUrl) { this.justificatifUrl = justificatifUrl; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
}
