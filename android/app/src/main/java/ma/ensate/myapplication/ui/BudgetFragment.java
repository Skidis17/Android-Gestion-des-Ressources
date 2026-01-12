package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.BudgetSummary;
import ma.ensate.myapplication.viewmodel.BudgetViewModel;

import java.text.DecimalFormat;

public class BudgetFragment extends Fragment {

    public BudgetFragment() { super(R.layout.fragment_budget); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTotalBudgetValue = view.findViewById(R.id.tvTotalBudgetValue);
        TextView tvRecettesValue = view.findViewById(R.id.tvRecettesValue);
        TextView tvDepensesValue = view.findViewById(R.id.tvDepensesValue);
        TextView tvSoldeValue = view.findViewById(R.id.tvSoldeValue);
        TextView tvPercentValue = view.findViewById(R.id.tvPercentValue);
        ProgressBar progressBudget = view.findViewById(R.id.progressBudget);
        ProgressBar loading = view.findViewById(R.id.loadingBudget);
        ImageView btnRefresh = view.findViewById(R.id.btnRefreshBudget);

        BudgetViewModel vm = new ViewModelProvider(this).get(BudgetViewModel.class);

        DecimalFormat df = new DecimalFormat("#,###");

        vm.getBudget().observe(getViewLifecycleOwner(), (BudgetSummary b) -> {
            if (b == null) {
                tvTotalBudgetValue.setText("—");
                tvRecettesValue.setText("—");
                tvDepensesValue.setText("—");
                tvSoldeValue.setText("—");
                tvPercentValue.setText("—");
                progressBudget.setProgress(0);
            } else {
                tvTotalBudgetValue.setText(formatMoney(df, b.montantTotal));
                tvRecettesValue.setText(formatMoney(df, b.totalRecettes));
                tvDepensesValue.setText(formatMoney(df, b.totalDepenses));
                tvSoldeValue.setText(formatMoney(df, b.montantDisponible));
                int perc = b.pourcentageUtilise == null ? 0 : (int) Math.round(b.pourcentageUtilise);
                tvPercentValue.setText(perc + "%");
                progressBudget.setProgress(Math.max(0, Math.min(100, perc)));
            }
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) loading.setVisibility(View.VISIBLE); else loading.setVisibility(View.GONE);
        });

        btnRefresh.setOnClickListener(v -> vm.loadBudget());

        // Initial load
        vm.loadBudget();
    }

    private String formatMoney(DecimalFormat df, Double value) {
        if (value == null) return "0 DH";
        return df.format(value.longValue()) + " DH";
    }
}
