package ma.ensate.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.BudgetSummary;
import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.BudgetViewModel;

import java.text.DecimalFormat;

public class BudgetFragment extends Fragment {

    public BudgetFragment() {
        super(R.layout.fragment_budget);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if user has access to budget
        TokenManager tokenManager = new TokenManager(requireContext());
        String role = tokenManager.getRole();
        boolean hasAccess = "directeur".equalsIgnoreCase(role) ||
                "Directeur_adjoint".equalsIgnoreCase(role) ||
                "secretaire_general".equalsIgnoreCase(role);

        View layoutAccessDenied = view.findViewById(R.id.layoutAccessDenied);
        View scrollBudgetContent = view.findViewById(R.id.scrollBudgetContent);
        View headerLayout = view.findViewById(R.id.headerLayout);

        if (!hasAccess) {
            // Show access denied, hide content
            layoutAccessDenied.setVisibility(View.VISIBLE);
            scrollBudgetContent.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            return;
        }

        // User has access, continue with normal setup
        layoutAccessDenied.setVisibility(View.GONE);
        scrollBudgetContent.setVisibility(View.VISIBLE);
        headerLayout.setVisibility(View.VISIBLE);

        TextView tvTotalBudgetValue = view.findViewById(R.id.tvTotalBudgetValue);
        TextView tvRecettesValue = view.findViewById(R.id.tvRecettesValue);
        TextView tvDepensesValue = view.findViewById(R.id.tvDepensesValue);
        TextView tvSoldeValue = view.findViewById(R.id.tvSoldeValue);
        TextView tvPercentValue = view.findViewById(R.id.tvPercentValue);
        TextView tvBudgetSummaryValue = view.findViewById(R.id.tvBudgetSummaryValue);
        ProgressBar progressBudget = view.findViewById(R.id.progressBudget);
        ProgressBar loading = view.findViewById(R.id.loadingBudget);
        ImageView btnRefresh = view.findViewById(R.id.btnRefreshBudget);
        MaterialCardView cardBudgetTotal = view.findViewById(R.id.cardBudgetTotal);

        BudgetViewModel vm = new ViewModelProvider(this).get(BudgetViewModel.class);

        DecimalFormat df = new DecimalFormat("#,###");

        // Check if user is director (already have role from above)
        boolean isDirecteur = "directeur".equalsIgnoreCase(role);

        // Add click listener for director to edit budget total
        if (isDirecteur) {
            cardBudgetTotal.setOnClickListener(v -> showEditBudgetDialog(vm));
        }

        vm.getBudget().observe(getViewLifecycleOwner(), (BudgetSummary b) -> {
            if (b == null) {
                tvTotalBudgetValue.setText("—");
                tvRecettesValue.setText("—");
                tvDepensesValue.setText("—");
                tvSoldeValue.setText("—");
                tvPercentValue.setText("—");
                tvBudgetSummaryValue.setText("—");
                progressBudget.setProgress(0);
            } else {
                tvTotalBudgetValue.setText(formatMoney(df, b.montantTotal));
                tvRecettesValue.setText(formatMoney(df, b.totalRecettes));
                tvDepensesValue.setText(formatMoney(df, b.totalDepenses));
                tvSoldeValue.setText(formatMoney(df, b.montantDisponible));

                // Calculate total available funds (Budget Total + Recettes)
                double totalAvailable = (b.montantTotal != null ? b.montantTotal : 0.0) +
                        (b.totalRecettes != null ? b.totalRecettes : 0.0);
                tvBudgetSummaryValue.setText(formatMoney(df, totalAvailable));

                int perc = b.pourcentageUtilise == null ? 0 : (int) Math.round(b.pourcentageUtilise);
                tvPercentValue.setText(perc + "%");
                progressBudget.setProgress(Math.max(0, Math.min(100, perc)));
            }
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading)
                loading.setVisibility(View.VISIBLE);
            else
                loading.setVisibility(View.GONE);
        });

        btnRefresh.setOnClickListener(v -> vm.loadBudget());

        // Initial load
        vm.loadBudget();
    }

    private String formatMoney(DecimalFormat df, Double value) {
        if (value == null)
            return "0 DH";
        return df.format(value.longValue()) + " DH";
    }

    private void showEditBudgetDialog(BudgetViewModel vm) {
        EditText input = new EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Nouveau montant total");

        new AlertDialog.Builder(requireContext())
                .setTitle("Modifier le budget total")
                .setMessage("Entrez le nouveau montant total du budget:")
                .setView(input)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    String value = input.getText().toString().trim();
                    if (!value.isEmpty()) {
                        try {
                            Double newTotal = Double.parseDouble(value);
                            if (newTotal < 0) {
                                Toast.makeText(requireContext(), "Le montant doit être positif", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            vm.updateBudgetTotal(newTotal, new BudgetViewModel.UpdateCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(requireContext(), "Budget mis à jour", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(), "Montant invalide", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
