package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Recette {
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
}