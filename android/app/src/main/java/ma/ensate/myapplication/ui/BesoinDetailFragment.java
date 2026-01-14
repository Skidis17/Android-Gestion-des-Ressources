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
        btnValidate.setOnClickListener(v -> showStatusChangeDialog("VALIDÉ", "Accepter le besoin"));
        btnReject.setOnClickListener(v -> showStatusChangeDialog("REFUSÉ", "Refuser le besoin"));
        btnApprove.setOnClickListener(v -> showStatusChangeDialog("APPROUVÉ", "Approuver le besoin"));
        btnTransmit.setOnClickListener(v -> createCommandeFromBesoin());
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
            case "TRANSMIS":
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

        // Get current user role
        String userRole = new ma.ensate.myapplication.network.TokenManager(requireContext()).getRole();
        if (userRole == null) return;

        // Show appropriate buttons based on status and role
        switch (statut) {
            case "EN_ATTENTE":
                // Only Directeur_adjoint can accept/refuse
                if ("Directeur_adjoint".equals(userRole) || "admin".equals(userRole)) {
                    btnValidate.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
                }
                break;
            case "VALIDÉ":
                // Only Directeur can approve/refuse
                if ("directeur".equals(userRole) || "admin".equals(userRole)) {
                    btnApprove.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
                }
                break;
            case "APPROUVÉ":
                // Only Secretaire_general can transmit
                if ("secretaire_general".equals(userRole) || "admin".equals(userRole)) {
                    btnTransmit.setVisibility(View.VISIBLE);
                }
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
        viewModel.changeStatus(besoin.getId(), newStatus, commentaire, new BesoinViewModel.ActionCallback() {
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

    private void createCommandeFromBesoin() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_commande, null);
        
        EditText etFournisseur = dialogView.findViewById(R.id.et_fournisseur);
        EditText etMontant = dialogView.findViewById(R.id.et_montant);
        EditText etDateLivraison = dialogView.findViewById(R.id.et_date_livraison);
        EditText etBonNumero = dialogView.findViewById(R.id.et_bon_numero);
        EditText etNotes = dialogView.findViewById(R.id.et_notes);
        TextView tvBesoinInfo = dialogView.findViewById(R.id.tv_besoin_info);
        
        // Pre-fill from besoin
        if (besoin.getMontantEstime() != null) {
            etMontant.setText(besoin.getMontantEstime().toString());
        }
        if (besoin.getDateLivraison() != null) {
            etDateLivraison.setText(besoin.getDateLivraison());
        }
        if (besoin.getDescription() != null) {
            etNotes.setText("Commande créée depuis le besoin: " + besoin.getDescription());
        }
        
        tvBesoinInfo.setText("Besoin #" + besoin.getId() + ": " + besoin.getDescription());
        
        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Créer", (dialog, which) -> {
                    String fournisseur = etFournisseur.getText().toString().trim();
                    if (fournisseur.isEmpty()) {
                        Toast.makeText(requireContext(), "Le fournisseur est obligatoire", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    java.util.Map<String, String> request = new java.util.HashMap<>();
                    request.put("fournisseur", fournisseur);
                    
                    String montant = etMontant.getText().toString().trim();
                    if (!montant.isEmpty()) {
                        request.put("montantTotal", montant);
                    }
                    
                    String dateLivraison = etDateLivraison.getText().toString().trim();
                    if (!dateLivraison.isEmpty()) {
                        request.put("dateLivraisonPrevue", dateLivraison);
                    }
                    
                    String bonNumero = etBonNumero.getText().toString().trim();
                    if (!bonNumero.isEmpty()) {
                        request.put("bonCommandeNumero", bonNumero);
                    }
                    
                    String notes = etNotes.getText().toString().trim();
                    if (!notes.isEmpty()) {
                        request.put("notes", notes);
                    }
                    
                    viewModel.repository.createCommandeFromBesoin(besoin.getId(), request).enqueue(new Callback<ma.ensate.myapplication.model.Commande>() {
                        @Override
                        public void onResponse(Call<ma.ensate.myapplication.model.Commande> call, Response<ma.ensate.myapplication.model.Commande> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Commande créée avec succès", Toast.LENGTH_SHORT).show();
                                // Reload besoin to update status to TRANSMIS
                                loadBesoin(besoin.getId());
                            } else {
                                String errorMsg = "Erreur lors de la création";
                                if (response.code() == 403) {
                                    errorMsg = "Vous n'avez pas les permissions nécessaires";
                                } else if (response.code() == 400) {
                                    errorMsg = "Le besoin doit être approuvé";
                                }
                                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ma.ensate.myapplication.model.Commande> call, Throwable t) {
                            Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}

