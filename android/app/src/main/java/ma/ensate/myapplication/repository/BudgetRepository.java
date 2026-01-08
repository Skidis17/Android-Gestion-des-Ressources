package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.BudgetSummary;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

public class BudgetRepository {
    private final ApiService api;

    public BudgetRepository() { this.api = RetrofitClient.api(); }

    public Call<BudgetSummary> getBudgetSummary() { return api.getBudgetSummary(); }
}
