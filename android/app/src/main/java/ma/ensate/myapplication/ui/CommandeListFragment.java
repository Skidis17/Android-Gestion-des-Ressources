package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.adapter.CommandeAdapter;
import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.CommandeViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandeListFragment extends Fragment {

    private CommandeViewModel viewModel;
    private CommandeAdapter adapter;
    private List<Commande> allCommandes = new ArrayList<>();
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commandes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Check role access
        String userRole = new TokenManager(requireContext()).getRole();
        if (!hasCommandeAccess(userRole)) {
            TextView tvMessage = new TextView(requireContext());
            tvMessage.setText("Vous n'avez pas accès à cette page");
            tvMessage.setTextSize(18);
            tvMessage.setPadding(32, 32, 32, 32);
            ((ViewGroup) view).removeAllViews();
            ((ViewGroup) view).addView(tvMessage);
            Toast.makeText(requireContext(), "Accès refusé", Toast.LENGTH_SHORT).show();
            return;
        }
        
        RecyclerView rv = view.findViewById(R.id.rv_commandes);
        adapter = new CommandeAdapter();
        adapter.setOnCommandeClickListener(this::showCommandeEditDialog);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // Search bar
        EditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase();
                filterCommandes();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        viewModel = new ViewModelProvider(this).get(CommandeViewModel.class);
        viewModel.getCommandes().observe(getViewLifecycleOwner(), commandes -> {
            allCommandes = commandes != null ? commandes : new ArrayList<>();
            filterCommandes();
        });
        viewModel.loadCommandes();
    }

    private void showCommandeEditDialog(Commande commande) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_commande, null);
        
        EditText etFournisseur = dialogView.findViewById(R.id.et_fournisseur);
        android.widget.Spinner spinnerStatut = dialogView.findViewById(R.id.spinner_statut);
        EditText etNotes = dialogView.findViewById(R.id.et_notes);
        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        TextView tvMontant = dialogView.findViewById(R.id.tv_montant_info);
        TextView tvDates = dialogView.findViewById(R.id.tv_dates_info);
        
        // Setup status spinner
        String[] statusOptions = {"EN_COURS", "LIVRÉ", "ANNULÉ"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatut.setAdapter(adapter);
        
        // Populate fields
        tvTitle.setText("Modifier Commande #" + commande.getId());
        etFournisseur.setText(commande.getFournisseur());
        etNotes.setText(commande.getNotes());
        
        // Set current status in spinner
        String currentStatus = commande.getStatut() != null ? commande.getStatut() : "EN_COURS";
        for (int i = 0; i < statusOptions.length; i++) {
            if (statusOptions[i].equals(currentStatus)) {
                spinnerStatut.setSelection(i);
                break;
            }
        }
        
        // Show read-only info
        String montant = commande.getMontantTotal() != null ? commande.getMontantTotal().toString() : "0.00";
        tvMontant.setText("Montant: " + montant + " MAD");
        
        String dateInfo = "Date: " + (commande.getDateCommande() != null ? commande.getDateCommande() : "N/A") +
                         "\nLivraison prévue: " + (commande.getDateLivraisonPrevue() != null ? commande.getDateLivraisonPrevue() : "N/A");
        tvDates.setText(dateInfo);
        
        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    String fournisseur = etFournisseur.getText().toString().trim();
                    String statut = spinnerStatut.getSelectedItem().toString();
                    String notes = etNotes.getText().toString().trim();
                    
                    if (fournisseur.isEmpty()) {
                        Toast.makeText(requireContext(), "Le fournisseur est obligatoire", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    updateCommande(commande.getId(), fournisseur, statut, notes);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
    
    private void updateCommande(Long id, String fournisseur, String statut, String notes) {
        Map<String, String> request = new HashMap<>();
        request.put("fournisseur", fournisseur);
        if (!TextUtils.isEmpty(statut)) {
            request.put("statut", statut);
        }
        if (!TextUtils.isEmpty(notes)) {
            request.put("notes", notes);
        }
        
        viewModel.repository.updateCommandeLimited(id, request).enqueue(new Callback<Commande>() {
            @Override
            public void onResponse(Call<Commande> call, Response<Commande> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Commande mise à jour", Toast.LENGTH_SHORT).show();
                    viewModel.loadCommandes(); // Reload list
                } else {
                    Toast.makeText(requireContext(), "Erreur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Commande> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterCommandes() {
        List<Commande> filtered = new ArrayList<>();
        for (Commande c : allCommandes) {
            if (!searchQuery.isEmpty()) {
                String fournisseur = c.getFournisseur() != null ? c.getFournisseur().toLowerCase() : "";
                String notes = c.getNotes() != null ? c.getNotes().toLowerCase() : "";
                String statut = c.getStatut() != null ? c.getStatut().toLowerCase() : "";
                
                if (!fournisseur.contains(searchQuery) && !notes.contains(searchQuery) && !statut.contains(searchQuery)) {
                    continue;
                }
            }
            filtered.add(c);
        }
        adapter.setItems(filtered);
    }

    private boolean hasCommandeAccess(String role) {
        if (role == null) return false;
        return role.equals("secretaire_general") || role.equals("Directeur_adjoint") || 
               role.equals("directeur") || role.equals("admin");
    }
}