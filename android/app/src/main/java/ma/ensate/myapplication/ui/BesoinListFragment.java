package ma.ensate.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.adapter.BesoinAdapter;
import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.viewmodel.BesoinViewModel;

public class BesoinListFragment extends Fragment {

    private BesoinViewModel viewModel;
    private BesoinAdapter adapter;
    private List<Besoin> allBesoins = new ArrayList<>();
    private String searchQuery = "";
    private List<String> selectedStatuses = new ArrayList<>();
    private List<String> selectedPriorities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_besoins, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv_besoins);
        adapter = new BesoinAdapter();
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
                filterBesoins();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter chips
        Chip chipEnAttente = view.findViewById(R.id.chip_en_attente);
        Chip chipValide = view.findViewById(R.id.chip_valide);
        Chip chipApprouve = view.findViewById(R.id.chip_approuve);
        Chip chipRefuse = view.findViewById(R.id.chip_refuse);
        Chip chipTransmis = view.findViewById(R.id.chip_transmis);

        Chip chipHaute = view.findViewById(R.id.chip_high);
        Chip chipMoyenne = view.findViewById(R.id.chip_moyenne);
        Chip chipBasse = view.findViewById(R.id.chip_low);

        chipEnAttente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedStatuses.add("EN_ATTENTE"); else selectedStatuses.remove("EN_ATTENTE");
            filterBesoins();
        });
        chipValide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedStatuses.add("VALIDÉ"); else selectedStatuses.remove("VALIDÉ");
            filterBesoins();
        });
        chipApprouve.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedStatuses.add("APPROUVÉ"); else selectedStatuses.remove("APPROUVÉ");
            filterBesoins();
        });
        chipRefuse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedStatuses.add("REFUSÉ"); else selectedStatuses.remove("REFUSÉ");
            filterBesoins();
        });
        chipTransmis.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedStatuses.add("TRANSMIS_A_ECO"); else selectedStatuses.remove("TRANSMIS_A_ECO");
            filterBesoins();
        });

        chipHaute.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedPriorities.add("HAUTE"); else selectedPriorities.remove("HAUTE");
            filterBesoins();
        });
        chipMoyenne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedPriorities.add("MOYENNE"); else selectedPriorities.remove("MOYENNE");
            filterBesoins();
        });
        chipBasse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedPriorities.add("BASSE"); else selectedPriorities.remove("BASSE");
            filterBesoins();
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_besoin);
        fab.setOnClickListener(v -> {
            // Navigate to create form (no besoinId argument - will be null)
            Navigation.findNavController(view).navigate(R.id.besoinFormFragment);
        });

        viewModel = new ViewModelProvider(this).get(BesoinViewModel.class);
        viewModel.getBesoins().observe(getViewLifecycleOwner(), besoins -> {
            allBesoins = besoins != null ? besoins : new ArrayList<>();
            filterBesoins();
        });
        
        // Set up adapter listeners
        adapter.setOnItemClickListener(besoin -> {
            // Navigate to detail view
            Bundle args = new Bundle();
            if (besoin.getId() != null) {
                args.putInt("besoinId", besoin.getId().intValue());
            }
            Navigation.findNavController(view).navigate(R.id.besoinDetailFragment, args);
        });

        adapter.setOnEditClickListener(besoin -> {
            // Only allow edit if status is EN_ATTENTE
            if (!"EN_ATTENTE".equals(besoin.getStatut())) {
                Toast.makeText(requireContext(), "Le besoin ne peut être modifié qu'en statut EN_ATTENTE", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle args = new Bundle();
            if (besoin.getId() != null) {
                args.putInt("besoinId", besoin.getId().intValue());
            }
            Navigation.findNavController(view).navigate(R.id.besoinFormFragment, args);
        });

        adapter.setOnDeleteClickListener(besoin -> {
            // Only allow delete if status is EN_ATTENTE
            if (!"EN_ATTENTE".equals(besoin.getStatut())) {
                Toast.makeText(requireContext(), "Le besoin ne peut être supprimé qu'en statut EN_ATTENTE", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(requireContext())
                    .setTitle("Supprimer le besoin")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce besoin ?")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        viewModel.deleteBesoin(besoin.getId(), new BesoinViewModel.ActionCallback() {
                            @Override
                            public void onSuccess(Besoin deleted) {
                                Toast.makeText(requireContext(), "Besoin supprimé", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Erreur lors de la suppression: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        viewModel.loadBesoins();
    }

    private void filterBesoins() {
        List<Besoin> filtered = new ArrayList<>();
        for (Besoin b : allBesoins) {
            // Apply status filter (if any selected)
            if (!selectedStatuses.isEmpty()) {
                if (!selectedStatuses.contains(b.getStatut())) {
                    continue;
                }
            }
            
            // Apply priority filter (if any selected)
            if (!selectedPriorities.isEmpty()) {
                String priority = b.getPriorite() != null ? b.getPriorite().toUpperCase() : "";
                if (!selectedPriorities.contains(priority)) {
                    continue;
                }
            }
            
            // Apply search query
            if (!searchQuery.isEmpty()) {
                String type = b.getTypeBesoin() != null ? b.getTypeBesoin().toLowerCase() : "";
                String desc = b.getDescription() != null ? b.getDescription().toLowerCase() : "";
                String statut = b.getStatut() != null ? b.getStatut().toLowerCase() : "";
                String priorite = b.getPriorite() != null ? b.getPriorite().toLowerCase() : "";
                
                if (!type.contains(searchQuery) && !desc.contains(searchQuery) && 
                    !statut.contains(searchQuery) && !priorite.contains(searchQuery)) {
                    continue;
                }
            }
            
            filtered.add(b);
        }
        adapter.setItems(filtered);
    }
}