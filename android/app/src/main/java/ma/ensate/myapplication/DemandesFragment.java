package ma.ensate.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.adapter.DemandeAdapter;
import ma.ensate.myapplication.model.Demande;
import ma.ensate.myapplication.model.PersonnelOption;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.DemandeViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemandesFragment extends Fragment {
    private DemandeViewModel viewModel;
    private DemandeAdapter adapter;
    private String currentStatut = "EN_ATTENTE"; // Default to EN_ATTENTE
    private List<Demande> allDemandes = new ArrayList<>(); // For filtering
    private String searchQuery = "";
    private String selectedPersonnelName = null; // null = all users
    private List<PersonnelOption> personnelOptions = new ArrayList<>();

    public DemandesFragment() {
        super(R.layout.fragment_demandes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Check if user has RH role
        TokenManager tokenManager = new TokenManager(requireContext());
        String role = tokenManager.getRole();
        if (role == null || !role.equalsIgnoreCase("RH")) {
            Toast.makeText(requireContext(), "Accès réservé aux ressources humaines", Toast.LENGTH_LONG).show();
            NavController nav = Navigation.findNavController(view);
            nav.popBackStack();
            return;
        }

        viewModel = new ViewModelProvider(requireActivity()).get(DemandeViewModel.class);
        adapter = new DemandeAdapter();

        RecyclerView rv = view.findViewById(R.id.rvDemandes);
        TextView empty = view.findViewById(R.id.tvEmptyDemandes);
        ProgressBar progress = view.findViewById(R.id.progressDemandes);
        MaterialCardView btnAdd = view.findViewById(R.id.btnAddDemande);
        
        // Stats views
        TextView tvStatsTotal = view.findViewById(R.id.tvStatsTotal);
        TextView tvStatsPending = view.findViewById(R.id.tvStatsPending);
        TextView tvStatsAccepted = view.findViewById(R.id.tvStatsAccepted);
        TextView tvStatsRejected = view.findViewById(R.id.tvStatsRejected);

        MaterialCardView filterAll = view.findViewById(R.id.filterAll);
        MaterialCardView filterPending = view.findViewById(R.id.filterPending);
        MaterialCardView filterAccepted = view.findViewById(R.id.filterAccepted);
        MaterialCardView filterRejected = view.findViewById(R.id.filterRejected);
        TextView tvAll = view.findViewById(R.id.tvFilterAll);
        TextView tvPending = view.findViewById(R.id.tvFilterPending);
        TextView tvAccepted = view.findViewById(R.id.tvFilterAccepted);
        TextView tvRejected = view.findViewById(R.id.tvFilterRejected);

        // Search and filter views
        EditText etSearch = view.findViewById(R.id.etSearchDemandes);
        AutoCompleteTextView actvFilterUser = view.findViewById(R.id.actvFilterUser);

        NavController nav = Navigation.findNavController(view);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(demande -> {
            Long id = demande.getId();
            if (id == null || id <= 0) {
                Toast.makeText(requireContext(), "Demande sans ID", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle args = new Bundle();
            args.putLong("demandeId", id);
            nav.navigate(R.id.demandeDetailFragment, args);
        });
        btnAdd.setOnClickListener(v -> nav.navigate(R.id.demandeFormFragment));

        filterAll.setOnClickListener(v -> applyFilter(null, filterAll, tvAll, filterPending, tvPending, filterAccepted, tvAccepted, filterRejected, tvRejected));
        filterPending.setOnClickListener(v -> applyFilter("EN_ATTENTE", filterPending, tvPending, filterAll, tvAll, filterAccepted, tvAccepted, filterRejected, tvRejected));
        filterAccepted.setOnClickListener(v -> applyFilter("ACCEPTEE", filterAccepted, tvAccepted, filterAll, tvAll, filterPending, tvPending, filterRejected, tvRejected));
        filterRejected.setOnClickListener(v -> applyFilter("REFUSEE", filterRejected, tvRejected, filterAll, tvAll, filterPending, tvPending, filterAccepted, tvAccepted));

        // Setup search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase();
                filterDemandes();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup user filter
        loadPersonnelOptions(actvFilterUser);

        viewModel.getDemandes().observe(getViewLifecycleOwner(), demandes -> {
            if (demandes != null) {
                allDemandes = new ArrayList<>(demandes);
                filterDemandes();
            } else {
                allDemandes = new ArrayList<>();
                filterDemandes();
            }
        });

        // Observe all demandes for stats
        viewModel.getAllDemandesForStats().observe(getViewLifecycleOwner(), allDemandes -> {
            updateStats(allDemandes != null ? allDemandes : new ArrayList<>(), 
                       tvStatsTotal, tvStatsPending, tvStatsAccepted, tvStatsRejected);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progress.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
        });

        // Default to EN_ATTENTE filter
        applyFilter("EN_ATTENTE", filterPending, tvPending, filterAll, tvAll, filterAccepted, tvAccepted, filterRejected, tvRejected);
    }
    
    private void updateStats(List<Demande> demandes, TextView total, TextView pending, TextView accepted, TextView rejected) {
        if (demandes == null) {
            total.setText("0");
            pending.setText("0");
            accepted.setText("0");
            rejected.setText("0");
            return;
        }
        
        int totalCount = demandes.size();
        int pendingCount = 0;
        int acceptedCount = 0;
        int rejectedCount = 0;
        
        for (Demande d : demandes) {
            String statut = d.getStatut();
            if ("EN_ATTENTE".equalsIgnoreCase(statut)) {
                pendingCount++;
            } else if ("ACCEPTEE".equalsIgnoreCase(statut)) {
                acceptedCount++;
            } else if ("REFUSEE".equalsIgnoreCase(statut)) {
                rejectedCount++;
            }
        }
        
        total.setText(String.valueOf(totalCount));
        pending.setText(String.valueOf(pendingCount));
        accepted.setText(String.valueOf(acceptedCount));
        rejected.setText(String.valueOf(rejectedCount));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load all demandes for stats
        viewModel.loadAllDemandesForStats();
        // Load filtered list
        viewModel.loadDemandes(currentStatut);
    }

    private void applyFilter(String statut,
                             MaterialCardView selectedCard, TextView selectedText,
                             MaterialCardView other1, TextView otherText1,
                             MaterialCardView other2, TextView otherText2,
                             MaterialCardView other3, TextView otherText3) {
        currentStatut = statut;
        setSelected(selectedCard, selectedText, true);
        setSelected(other1, otherText1, false);
        setSelected(other2, otherText2, false);
        setSelected(other3, otherText3, false);
        viewModel.loadDemandes(currentStatut);
    }

    private void setSelected(MaterialCardView card, TextView text, boolean selected) {
        if (selected) {
            card.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            text.setTextColor(Color.WHITE);
        } else {
            card.setCardBackgroundColor(Color.WHITE);
            text.setTextColor(Color.parseColor("#6B7280"));
        }
    }

    private void filterDemandes() {
        List<Demande> filtered = new ArrayList<>();
        for (Demande d : allDemandes) {
            // Filter by personnel name
            if (selectedPersonnelName != null && !selectedPersonnelName.equals("Tous les personnels")) {
                String demandePersonnelName = d.getCreatedByName();
                if (demandePersonnelName == null || !demandePersonnelName.equals(selectedPersonnelName)) {
                    continue;
                }
            }

            // Filter by search query
            if (!searchQuery.isEmpty()) {
                String type = d.getType() != null ? d.getType().toLowerCase() : "";
                String motif = d.getMotif() != null ? d.getMotif().toLowerCase() : "";
                String personnelName = d.getCreatedByName() != null ? d.getCreatedByName().toLowerCase() : "";
                String statut = d.getStatut() != null ? d.getStatut().toLowerCase() : "";
                
                if (!type.contains(searchQuery) && !motif.contains(searchQuery) && 
                    !personnelName.contains(searchQuery) && !statut.contains(searchQuery)) {
                    continue;
                }
            }

            filtered.add(d);
        }
        
        adapter.submit(filtered);
        TextView empty = getView() != null ? getView().findViewById(R.id.tvEmptyDemandes) : null;
        if (empty != null) {
            empty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void loadPersonnelOptions(AutoCompleteTextView actv) {
        TokenManager tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        if (token == null) return;

        ApiService api = RetrofitClient.api();
        api.getAllPersonnels("Bearer " + token).enqueue(new Callback<List<PersonnelOption>>() {
            @Override
            public void onResponse(Call<List<PersonnelOption>> call, Response<List<PersonnelOption>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    personnelOptions = response.body();
                    // Create adapter with personnel names
                    List<String> options = new ArrayList<>();
                    options.add("Tous les personnels");
                    for (PersonnelOption p : personnelOptions) {
                        options.add(p.getFullName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
                        android.R.layout.simple_dropdown_item_1line, options);
                    actv.setAdapter(adapter);
                    
                    // Handle selection
                    actv.setOnItemClickListener((parent, view, position, id) -> {
                        if (position == 0) {
                            selectedPersonnelName = null;
                        } else {
                            PersonnelOption selected = personnelOptions.get(position - 1);
                            selectedPersonnelName = selected.getFullName();
                        }
                        filterDemandes();
                    });
                    
                    // Make dropdown show on click
                    actv.setThreshold(0);
                    actv.setOnClickListener(v -> actv.showDropDown());
                }
            }

            @Override
            public void onFailure(Call<List<PersonnelOption>> call, Throwable t) {
                // Silently fail - filter just won't work
            }
        });
    }

}
