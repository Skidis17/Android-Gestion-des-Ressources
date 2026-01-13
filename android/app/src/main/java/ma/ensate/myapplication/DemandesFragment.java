package ma.ensate.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

import java.util.List;

import ma.ensate.myapplication.adapter.DemandeAdapter;
import ma.ensate.myapplication.model.Demande;
import ma.ensate.myapplication.viewmodel.DemandeViewModel;

public class DemandesFragment extends Fragment {
    private DemandeViewModel viewModel;
    private DemandeAdapter adapter;
    private String currentStatut = null;

    public DemandesFragment() {
        super(R.layout.fragment_demandes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DemandeViewModel.class);
        adapter = new DemandeAdapter();

        RecyclerView rv = view.findViewById(R.id.rvDemandes);
        TextView empty = view.findViewById(R.id.tvEmptyDemandes);
        TextView subtitle = view.findViewById(R.id.tvDemandesSubtitle);
        ProgressBar progress = view.findViewById(R.id.progressDemandes);
        MaterialCardView btnAdd = view.findViewById(R.id.btnAddDemande);

        MaterialCardView filterAll = view.findViewById(R.id.filterAll);
        MaterialCardView filterPending = view.findViewById(R.id.filterPending);
        MaterialCardView filterAccepted = view.findViewById(R.id.filterAccepted);
        MaterialCardView filterRejected = view.findViewById(R.id.filterRejected);
        TextView tvAll = view.findViewById(R.id.tvFilterAll);
        TextView tvPending = view.findViewById(R.id.tvFilterPending);
        TextView tvAccepted = view.findViewById(R.id.tvFilterAccepted);
        TextView tvRejected = view.findViewById(R.id.tvFilterRejected);

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

        viewModel.getDemandes().observe(getViewLifecycleOwner(), demandes -> {
            adapter.submit(demandes);
            empty.setVisibility(demandes == null || demandes.isEmpty() ? View.VISIBLE : View.GONE);
            subtitle.setText(countPending(demandes) + " en attente");
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progress.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
        });

        applyFilter(null, filterAll, tvAll, filterPending, tvPending, filterAccepted, tvAccepted, filterRejected, tvRejected);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private int countPending(List<Demande> demandes) {
        if (demandes == null) return 0;
        int count = 0;
        for (Demande d : demandes) {
            if ("EN_ATTENTE".equalsIgnoreCase(d.getStatut())) count++;
        }
        return count;
    }
}
