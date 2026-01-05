package ma.ensate.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.viewmodel.BesoinViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BesoinDetailFragment extends Fragment {

    private BesoinViewModel viewModel;
    private Besoin besoin;
    private TextView tvStatus, tvType, tvDescription, tvQuantite, tvMontant, tvPriorite, tvDateLivraison;
    private TextView tvCommentaire, tvDateDemande, tvDateTraitement;
    private Button btnValidate, btnReject, btnApprove, btnTransmit, btnAddComment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_besoin_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvStatus = view.findViewById(R.id.tv_status);
        tvType = view.findViewById(R.id.tv_type);
        tvDescription = view.findViewById(R.id.tv_description);
        tvQuantite = view.findViewById(R.id.tv_quantite);
        tvMontant = view.findViewById(R.id.tv_montant);
        tvPriorite = view.findViewById(R.id.tv_priorite);
        tvDateLivraison = view.findViewById(R.id.tv_date_livraison);
        tvCommentaire = view.findViewById(R.id.tv_commentaire);
        tvDateDemande = view.findViewById(R.id.tv_date_demande);
        tvDateTraitement = view.findViewById(R.id.tv_date_traitement);

        btnValidate = view.findViewById(R.id.btn_validate);
        btnReject = view.findViewById(R.id.btn_reject);
        btnApprove = view.findViewById(R.id.btn_approve);
        btnTransmit = view.findViewById(R.id.btn_transmit);
        btnAddComment = view.findViewById(R.id.btn_add_comment);

        viewModel = new ViewModelProvider(requireActivity()).get(BesoinViewModel.class);

        // Get besoin ID from arguments
        if (getArguments() != null && getArguments().containsKey("besoinId")) {
            int besoinId = getArguments().getInt("besoinId", -1);
            if (besoinId > 0) {
                loadBesoin((long) besoinId);
            }
        }

        // Set up action buttons
        btnValidate.setOnClickListener(v -> showStatusChangeDialog("VALIDÉ", "Valider le besoin"));
        btnReject.setOnClickListener(v -> showStatusChangeDialog("REFUSÉ", "Refuser le besoin"));
        btnApprove.setOnClickListener(v -> showStatusChangeDialog("APPROUVÉ", "Approuver le besoin"));
        btnTransmit.setOnClickListener(v -> showStatusChangeDialog("TRANSMIS_A_ECO", "Transmettre à Économique"));
        btnAddComment.setOnClickListener(v -> showAddCommentDialog());
    }

    private void loadBesoin(Long id) {
        viewModel.repository.getBesoin(id).enqueue(new Callback<Besoin>() {
            @Override
            public void onResponse(Call<Besoin> call, Response<Besoin> response) {
                if (response.isSuccessful() && response.body() != null) {
                    besoin = response.body();
                    populateViews();
                } else {
                    Toast.makeText(requireContext(), "Erreur lors du chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Besoin> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateViews() {
        if (besoin == null) return;

        // Status
        String statut = besoin.getStatut() != null ? besoin.getStatut() : "EN_ATTENTE";
        tvStatus.setText(statut);
        updateStatusColor(statut);

        // Basic info
        tvType.setText("Type: " + (besoin.getTypeBesoin() != null ? besoin.getTypeBesoin() : "N/A"));
        tvDescription.setText("Description: " + (besoin.getDescription() != null ? besoin.getDescription() : "N/A"));
        tvQuantite.setText("Quantité: " + (besoin.getQuantite() != null ? besoin.getQuantite().toString() : "N/A"));
        tvMontant.setText("Montant estimé: " + (besoin.getMontantEstime() != null ? besoin.getMontantEstime().toString() : "N/A"));
        tvPriorite.setText("Priorité: " + (besoin.getPriorite() != null ? besoin.getPriorite() : "N/A"));
        tvDateLivraison.setText("Date de livraison: " + (besoin.getDateLivraison() != null ? besoin.getDateLivraison() : "N/A"));

        // Comments
        if (!TextUtils.isEmpty(besoin.getCommentaireAdmin())) {
            tvCommentaire.setText(besoin.getCommentaireAdmin());
            tvCommentaire.setTextColor(0xFF000000); // Black
        } else {
            tvCommentaire.setText("Aucun commentaire");
            tvCommentaire.setTextColor(0xFF666666); // Gray
        }

        // Dates
        tvDateDemande.setText("Date de demande: " + (besoin.getDateDemande() != null ? besoin.getDateDemande() : "N/A"));
        tvDateTraitement.setText("Date de traitement: " + (besoin.getDateTraitement() != null ? besoin.getDateTraitement() : "N/A"));

        // Show/hide action buttons based on status
        updateActionButtons(statut);
    }

    private void updateStatusColor(String statut) {
        int color;
        switch (statut) {
            case "EN_ATTENTE":
                color = 0xFFF59E0B; // Orange
                break;
            case "VALIDÉ":
                color = 0xFF3B82F6; // Blue
                break;
            case "APPROUVÉ":
                color = 0xFF10B981; // Green
                break;
            case "TRANSMIS_A_ECO":
                color = 0xFF8B5CF6; // Purple
                break;
            case "REFUSÉ":
                color = 0xFFEF4444; // Red
                break;
            default:
                color = 0xFF6B7280; // Gray
        }
        tvStatus.setTextColor(color);
    }

    private void updateActionButtons(String statut) {
        // Hide all buttons first
        btnValidate.setVisibility(View.GONE);
        btnReject.setVisibility(View.GONE);
        btnApprove.setVisibility(View.GONE);
        btnTransmit.setVisibility(View.GONE);

        // Show appropriate buttons based on status
        switch (statut) {
            case "EN_ATTENTE":
                btnValidate.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                break;
            case "VALIDÉ":
                btnApprove.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                break;
            case "APPROUVÉ":
                btnTransmit.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showStatusChangeDialog(String newStatus, String title) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_status_change, null);
        EditText etComment = dialogView.findViewById(R.id.et_comment);

        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("Confirmer", (dialog, which) -> {
                    String comment = etComment.getText().toString().trim();
                    changeStatus(newStatus, comment);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showAddCommentDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_status_change, null);
        EditText etComment = dialogView.findViewById(R.id.et_comment);
        if (besoin.getCommentaireAdmin() != null) {
            etComment.setText(besoin.getCommentaireAdmin());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Ajouter un commentaire")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    String comment = etComment.getText().toString().trim();
                    // Update comment without changing status
                    changeStatus(besoin.getStatut(), comment);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void changeStatus(String newStatus, String commentaire) {
        Long traitePar = 1L; // TODO: Get from logged-in user
        viewModel.changeStatus(besoin.getId(), newStatus, traitePar, commentaire, new BesoinViewModel.ActionCallback() {
            @Override
            public void onSuccess(Besoin updated) {
                besoin = updated;
                populateViews();
                Toast.makeText(requireContext(), "Statut mis à jour", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

