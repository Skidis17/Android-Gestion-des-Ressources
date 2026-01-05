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
import ma.ensate.myapplication.adapter.DepenseAdapter;
import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.viewmodel.DepenseViewModel;

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
}