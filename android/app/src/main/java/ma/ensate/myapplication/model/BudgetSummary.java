package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class BudgetSummary {

    @SerializedName(value = "montantTotal", alternate = {"montant_total", "totalBudget", "total_budget", "total"})
    public Double montantTotal;

    @SerializedName(value = "totalRecettes", alternate = {"total_recettes", "recettes"})
    public Double totalRecettes;

    @SerializedName(value = "totalDepenses", alternate = {"total_depenses", "depenses"})
    public Double totalDepenses;

    @SerializedName(value = "montantDisponible", alternate = {"montant_disponible", "soldeDisponible", "solde"})
    public Double montantDisponible;

    @SerializedName(value = "pourcentageUtilise", alternate = {"pourcentage_utilise", "usedPercentage", "percentageUsed", "percent"})
    public Double pourcentageUtilise;

}
