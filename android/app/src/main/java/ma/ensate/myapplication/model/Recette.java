package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Recette implements Serializable {
    public Long id;

    @SerializedName("source")
    public String source;

    @SerializedName("categorie")
    public String categorie;

    @SerializedName("montant")
    public Double montant;

    @SerializedName("date")
    public String date; // ISO string for now

    @SerializedName("description")
    public String description;

    @SerializedName("reference")
    public String reference;

    // Getters
    public Long getId() { return id; }
    public String getSource() { return source; }
    public String getCategorie() { return categorie; }
    public Double getMontant() { return montant; }
    public String getDateRecette() { return date; }
    public String getDescription() { return description; }
    public String getReferenceDocument() { return reference; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSource(String source) { this.source = source; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setMontant(Double montant) { this.montant = montant; }
    public void setDateRecette(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setReferenceDocument(String reference) { this.reference = reference; }
}