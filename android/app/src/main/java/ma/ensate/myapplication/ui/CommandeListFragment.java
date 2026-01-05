package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.adapter.CommandeAdapter;
import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.viewmodel.CommandeViewModel;

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
        RecyclerView rv = view.findViewById(R.id.rv_commandes);
        adapter = new CommandeAdapter();
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
}