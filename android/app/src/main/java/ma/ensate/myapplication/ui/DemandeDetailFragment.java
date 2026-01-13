package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Demande;
import ma.ensate.myapplication.viewmodel.DemandeViewModel;

public class DemandeDetailFragment extends Fragment {
    public DemandeDetailFragment() {
        super(R.layout.fragment_demande_detail);
    }

    private Long demandeId;
    private DemandeViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        demandeId = getArguments() != null ? getArguments().getLong("demandeId", -1) : -1;
        if (demandeId == -1) {
            Toast.makeText(requireContext(), "Demande manquante", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        viewModel = new ViewModelProvider(this).get(DemandeViewModel.class);

        TextView tvType = view.findViewById(R.id.tv_demande_type);
        TextView tvDates = view.findViewById(R.id.tv_demande_dates);
        TextView tvMotif = view.findViewById(R.id.tv_demande_motif);
        TextView tvJustificatif = view.findViewById(R.id.tv_demande_justificatif);
        TextView tvStatut = view.findViewById(R.id.tv_demande_statut);
        TextView tvCreatedBy = view.findViewById(R.id.tv_demande_created_by);
        TextView tvCreatedAt = view.findViewById(R.id.tv_demande_created_at);
        Button btnAccept = view.findViewById(R.id.btn_demande_accept);
        Button btnRefuse = view.findViewById(R.id.btn_demande_refuse);
        Button btnPending = view.findViewById(R.id.btn_demande_pending);
        View btnBack = view.findViewById(R.id.btn_demande_back);

        btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        viewModel.getSelected().observe(getViewLifecycleOwner(), d -> {
            if (d == null) return;
            tvType.setText(d.getType());
            tvDates.setText("Du " + safe(d.getDateDebut()) + " au " + safe(d.getDateFin()));
            tvMotif.setText(d.getMotif());
            tvStatut.setText(d.getStatut() != null ? d.getStatut() : "EN_ATTENTE");
            // Show personnel name if available, otherwise fallback to ID
            if (d.getCreatedByName() != null && !d.getCreatedByName().trim().isEmpty()) {
                tvCreatedBy.setText(d.getCreatedByName());
            } else {
                tvCreatedBy.setText(d.getCreatedBy() != null ? "Employé #" + d.getCreatedBy() : "Employé inconnu");
            }
            tvCreatedAt.setText(d.getCreatedAt() != null ? d.getCreatedAt() : "-");

            if (d.getJustificatifUrl() != null && !d.getJustificatifUrl().isBlank()) {
                tvJustificatif.setVisibility(View.VISIBLE);
                tvJustificatif.setOnClickListener(v -> openLink(d.getJustificatifUrl()));
            } else {
                tvJustificatif.setVisibility(View.GONE);
            }
        });

        viewModel.loadById(demandeId);

        btnAccept.setOnClickListener(v -> updateStatus("ACCEPTEE"));
        btnRefuse.setOnClickListener(v -> updateStatus("REFUSEE"));
        btnPending.setOnClickListener(v -> updateStatus("EN_ATTENTE"));
    }

    private void updateStatus(String statut) {
        viewModel.updateStatus(demandeId, statut, new DemandeViewModel.ActionCallback() {
            @Override
            public void onSuccess(Demande created) {
                Toast.makeText(requireContext(), "Statut mis à jour", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
