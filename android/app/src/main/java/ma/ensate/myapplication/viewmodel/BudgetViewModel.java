package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.BudgetSummary;
import ma.ensate.myapplication.repository.BudgetRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetViewModel extends ViewModel {
    private final BudgetRepository repository = new BudgetRepository();
    private final MutableLiveData<BudgetSummary> budget = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<BudgetSummary> getBudget() { return budget; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void loadBudget() {
        loading.postValue(true);
        repository.getBudgetSummary().enqueue(new Callback<BudgetSummary>() {
            @Override
            public void onResponse(Call<BudgetSummary> call, Response<BudgetSummary> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    budget.postValue(response.body());
                } else {
                    budget.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<BudgetSummary> call, Throwable t) {
                loading.postValue(false);
                budget.postValue(null);
            }
        });
    }
}
