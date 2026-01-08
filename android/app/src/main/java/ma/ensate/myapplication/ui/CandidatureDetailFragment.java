package ma.ensate.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.viewmodel.CandidatureRecrutementViewModel;

public class CandidatureDetailFragment extends Fragment {
    public CandidatureDetailFragment() {
        super(R.layout.fragment_candidature_detail);
    }

    private Long candidatureId;
    private CandidatureRecrutementViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidature_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        candidatureId = getArguments() != null ? getArguments().getLong("candidatureId", -1) : -1;
        if (candidatureId == -1) {
            Toast.makeText(requireContext(), "Candidature manquante", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        viewModel = new ViewModelProvider(this).get(CandidatureRecrutementViewModel.class);

        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView tvCin = view.findViewById(R.id.tv_cin);
        TextView tvStatut = view.findViewById(R.id.tv_statut);
        TextView tvCv = view.findViewById(R.id.tv_cv_link);
        TextView tvLm = view.findViewById(R.id.tv_lm_link);
        Button btnAccept = view.findViewById(R.id.btn_accept);
        Button btnRefuse = view.findViewById(R.id.btn_refuse);
        Button btnPending = view.findViewById(R.id.btn_pending);
        SwitchMaterial swEmail = view.findViewById(R.id.sw_send_email);
        View btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        viewModel.getSelected().observe(getViewLifecycleOwner(), c -> {
            if (c == null) return;
            tvName.setText(c.getPrenom() + " " + c.getNom());
            tvEmail.setText(c.getEmail());
            tvCin.setText("CIN: " + c.getCin());
            tvStatut.setText(c.getStatut() != null ? c.getStatut() : "EN_ATTENTE");

            if (c.getCvUrl() != null && !c.getCvUrl().isBlank()) {
                tvCv.setVisibility(View.VISIBLE);
                tvCv.setOnClickListener(v -> openLink(c.getCvUrl()));
            } else {
                tvCv.setVisibility(View.GONE);
            }
            if (c.getLettreMotivationUrl() != null && !c.getLettreMotivationUrl().isBlank()) {
                tvLm.setVisibility(View.VISIBLE);
                tvLm.setOnClickListener(v -> openLink(c.getLettreMotivationUrl()));
            } else {
                tvLm.setVisibility(View.GONE);
            }
        });

        viewModel.loadById(candidatureId);

        btnAccept.setOnClickListener(v -> updateStatus("RETENU", swEmail.isChecked()));
        btnRefuse.setOnClickListener(v -> updateStatus("REFUSE", false));
        btnPending.setOnClickListener(v -> updateStatus("EN_ATTENTE", false));
    }

    private void updateStatus(String statut, boolean sendEmail) {
        viewModel.updateStatus(candidatureId, statut, sendEmail, new CandidatureRecrutementViewModel.ActionCallback() {
            @Override
            public void onSuccess(CandidatureRecrutement c) {
                Toast.makeText(requireContext(), "Statut mis à jour", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
