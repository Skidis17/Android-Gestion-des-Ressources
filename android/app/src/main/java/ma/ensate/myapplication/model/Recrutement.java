package ma.ensate.myapplication.model;

import java.time.LocalDateTime;

public class Recrutement {
    private Long id;
    private String poste;
    private String typeContrat;
    private String departement;
    private Integer nombrePostes;
    private String description;
    private String dateOuverture; // ISO yyyy-MM-dd
    private String dateCloture;   // ISO yyyy-MM-dd
    private String statut;
    private Long createdBy;
    private String createdAt; // ISO date-time

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }
    public String getTypeContrat() { return typeContrat; }
    public void setTypeContrat(String typeContrat) { this.typeContrat = typeContrat; }
    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
    public Integer getNombrePostes() { return nombrePostes; }
    public void setNombrePostes(Integer nombrePostes) { this.nombrePostes = nombrePostes; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDateOuverture() { return dateOuverture; }
    public void setDateOuverture(String dateOuverture) { this.dateOuverture = dateOuverture; }
    public String getDateCloture() { return dateCloture; }
    public void setDateCloture(String dateCloture) { this.dateCloture = dateCloture; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
