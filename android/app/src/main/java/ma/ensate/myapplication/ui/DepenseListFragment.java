package ma.ensate.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.adapter.DepenseAdapter;
import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.viewmodel.DepenseViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepenseListFragment extends Fragment {

    private DepenseViewModel viewModel;
    private DepenseAdapter adapter;
    private List<Depense> allDepenses = new ArrayList<>();
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_depenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv_depenses);
        adapter = new DepenseAdapter();
        adapter.setOnDepenseClickListener(depense -> showDepenseEditDialog(depense));
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);
        
        FloatingActionButton fab = view.findViewById(R.id.fab_add_depense);
        fab.setOnClickListener(v -> showDepenseAddDialog());

        // Search bar
        EditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase();
                filterDepenses();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);
        viewModel.getDepenses().observe(getViewLifecycleOwner(), depenses -> {
            allDepenses = depenses != null ? depenses : new ArrayList<>();
            filterDepenses();
        });
        viewModel.loadDepenses();
    }

    private void filterDepenses() {
        List<Depense> filtered = new ArrayList<>();
        for (Depense d : allDepenses) {
            if (!searchQuery.isEmpty()) {
                String categorie = d.getCategorie() != null ? d.getCategorie().toLowerCase() : "";
                String desc = d.getDescription() != null ? d.getDescription().toLowerCase() : "";
                String fournisseur = d.getFournisseur() != null ? d.getFournisseur().toLowerCase() : "";
                
                if (!categorie.contains(searchQuery) && !desc.contains(searchQuery) && !fournisseur.contains(searchQuery)) {
                    continue;
                }
            }
            filtered.add(d);
        }
        adapter.setItems(filtered);
    }
    
    private void showDepenseAddDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_edit_depense, null);
        
        EditText etCategorie = dialogView.findViewById(R.id.et_categorie);
        EditText etMontant = dialogView.findViewById(R.id.et_montant);
        EditText etDate = dialogView.findViewById(R.id.et_date);
        EditText etFournisseur = dialogView.findViewById(R.id.et_fournisseur);
        EditText etFacture = dialogView.findViewById(R.id.et_facture);
        EditText etModePaiement = dialogView.findViewById(R.id.et_mode_paiement);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Ajouter Dépense")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    Depense depense = new Depense();
                    depense.setCategorie(etCategorie.getText().toString().trim());
                    
                    String montantStr = etMontant.getText().toString().trim();
                    if (!montantStr.isEmpty()) {
                        try {
                            depense.setMontant(new java.math.BigDecimal(montantStr));
                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(), "Montant invalide", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    
                    String date = etDate.getText().toString().trim();
                    if (!date.isEmpty()) {
                        depense.setDateDepense(date);
                    }
                    
                    depense.setFournisseur(etFournisseur.getText().toString().trim());
                    depense.setFactureNumero(etFacture.getText().toString().trim());
                    depense.setModePaiement(etModePaiement.getText().toString().trim());
                    depense.setDescription(etDescription.getText().toString().trim());
                    
                    createDepense(depense);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
    
    private void showDepenseEditDialog(Depense depense) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_edit_depense, null);
        
        EditText etCategorie = dialogView.findViewById(R.id.et_categorie);
        EditText etMontant = dialogView.findViewById(R.id.et_montant);
        EditText etDate = dialogView.findViewById(R.id.et_date);
        EditText etFournisseur = dialogView.findViewById(R.id.et_fournisseur);
        EditText etFacture = dialogView.findViewById(R.id.et_facture);
        EditText etModePaiement = dialogView.findViewById(R.id.et_mode_paiement);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        
        // Pre-fill
        etCategorie.setText(depense.getCategorie());
        if (depense.getMontant() != null) {
            etMontant.setText(depense.getMontant().toString());
        }
        if (depense.getDateDepense() != null) {
            etDate.setText(depense.getDateDepense().toString());
        }
        etFournisseur.setText(depense.getFournisseur());
        etFacture.setText(depense.getFactureNumero());
        etModePaiement.setText(depense.getModePaiement());
        etDescription.setText(depense.getDescription());
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Modifier Dépense #" + depense.getId())
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    depense.setCategorie(etCategorie.getText().toString().trim());
                    
                    String montantStr = etMontant.getText().toString().trim();
                    if (!montantStr.isEmpty()) {
                        try {
                            depense.setMontant(new java.math.BigDecimal(montantStr));
                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(), "Montant invalide", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    
                    String date = etDate.getText().toString().trim();
                    if (!date.isEmpty()) {
                        depense.setDateDepense(date);
                    }
                    
                    depense.setFournisseur(etFournisseur.getText().toString().trim());
                    depense.setFactureNumero(etFacture.getText().toString().trim());
                    depense.setModePaiement(etModePaiement.getText().toString().trim());
                    depense.setDescription(etDescription.getText().toString().trim());
                    
                    updateDepense(depense);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
    
    private void createDepense(Depense depense) {
        viewModel.repository.createDepense(depense).enqueue(new Callback<Depense>() {
            @Override
            public void onResponse(Call<Depense> call, Response<Depense> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Dépense ajoutée", Toast.LENGTH_SHORT).show();
                    viewModel.loadDepenses();
                } else {
                    Toast.makeText(requireContext(), "Erreur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Depense> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateDepense(Depense depense) {
        viewModel.repository.updateDepense(depense.getId(), depense).enqueue(new Callback<Depense>() {
            @Override
            public void onResponse(Call<Depense> call, Response<Depense> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Dépense modifiée", Toast.LENGTH_SHORT).show();
                    viewModel.loadDepenses();
                } else {
                    Toast.makeText(requireContext(), "Erreur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Depense> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}