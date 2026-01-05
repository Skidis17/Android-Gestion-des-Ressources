package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class BesoinFormFragment extends Fragment {

    private BesoinViewModel viewModel;
    private EditText etType, etDescription, etQuantite, etMontant, etPriorite, etDateLivraison;
    private Button btnSave;
    private Long editId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_besoin_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etType = view.findViewById(R.id.et_type);
        etDescription = view.findViewById(R.id.et_description);
        etQuantite = view.findViewById(R.id.et_quantite);
        etMontant = view.findViewById(R.id.et_montant);
        etPriorite = view.findViewById(R.id.et_priorite);
        etDateLivraison = view.findViewById(R.id.et_date_livraison);
        btnSave = view.findViewById(R.id.btn_save);

        viewModel = new ViewModelProvider(requireActivity()).get(BesoinViewModel.class);

        // If arguments contain an id, load the besoin to edit
        if (getArguments() != null) {
            int besoinIdArg = getArguments().getInt("besoinId", -1);
            if (besoinIdArg > 0) {
                editId = (long) besoinIdArg;
                btnSave.setText("Mettre à jour");
                // load single besoin from API and populate fields
                viewModel.repository.getBesoin(editId).enqueue(new Callback<Besoin>() {
                    @Override
                    public void onResponse(Call<Besoin> call, Response<Besoin> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Besoin b = response.body();
                            // Check if editing is allowed (only EN_ATTENTE)
                            if (!"EN_ATTENTE".equals(b.getStatut())) {
                                Toast.makeText(requireContext(), "Le besoin ne peut être modifié qu'en statut EN_ATTENTE", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(view).popBackStack();
                                return;
                            }
                            etType.setText(b.getTypeBesoin());
                            etDescription.setText(b.getDescription());
                            if (b.getQuantite() != null) etQuantite.setText(String.valueOf(b.getQuantite()));
                            if (b.getMontantEstime() != null) etMontant.setText(b.getMontantEstime().toString());
                            etPriorite.setText(b.getPriorite());
                            etDateLivraison.setText(b.getDateLivraison());
                        }
                    }

                    @Override
                    public void onFailure(Call<Besoin> call, Throwable t) {
                        Toast.makeText(requireContext(), "Erreur lors du chargement du besoin", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                btnSave.setText("Créer");
            }
        } else {
            btnSave.setText("Créer");
        }

        btnSave.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etType.getText()) || TextUtils.isEmpty(etDescription.getText())) {
                Toast.makeText(requireContext(), "Type et description requis", Toast.LENGTH_SHORT).show();
                return;
            }
            Besoin b = new Besoin();
            // Set required fields - ALWAYS set personnelId first
            b.setPersonnelId(1L); // TODO: Get from logged-in user session
            // Trim all string fields to avoid trailing spaces
            b.setTypeBesoin(etType.getText().toString().trim());
            b.setDescription(etDescription.getText().toString().trim());
            if (!TextUtils.isEmpty(etQuantite.getText())) {
                try {
                    b.setQuantite(Integer.parseInt(etQuantite.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            if (!TextUtils.isEmpty(etMontant.getText())) {
                try {
                    b.setMontantEstime(new java.math.BigDecimal(etMontant.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            if (!TextUtils.isEmpty(etPriorite.getText())) {
                b.setPriorite(etPriorite.getText().toString().trim());
            }
            String dateLivStr = etDateLivraison.getText().toString().trim();
            if (!TextUtils.isEmpty(dateLivStr) && dateLivStr.length() >= 10) {
                // Ensure date format is YYYY-MM-DD (take first 10 characters)
                b.setDateLivraison(dateLivStr.substring(0, Math.min(10, dateLivStr.length())));
            } else {
                b.setDateLivraison(null);
            }

            if (editId == null) {
                // create: attempt network first, then fallback to local + sync
                viewModel.createBesoin(b, new BesoinViewModel.ActionCallback() {
                    @Override
                    public void onSuccess(ma.ensate.myapplication.model.Besoin created) {
                        // Network success: save to local as synced
                        ma.ensate.myapplication.repository.local.BesoinLocalRepository localRepo = new ma.ensate.myapplication.repository.local.BesoinLocalRepository(requireContext());
                        java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                            ma.ensate.myapplication.db.entity.BesoinEntity e = new ma.ensate.myapplication.db.entity.BesoinEntity();
                            e.setServerId(created.getId());
                            e.setPersonnelId(created.getPersonnelId());
                            e.setTypeBesoin(created.getTypeBesoin());
                            e.setDescription(created.getDescription());
                            e.setQuantite(created.getQuantite());
                            e.setMontantEstime(created.getMontantEstime() != null ? created.getMontantEstime().toPlainString() : null);
                            e.setPriorite(created.getPriorite());
                            e.setStatut(created.getStatut());
                            e.setCommentaireAdmin(created.getCommentaireAdmin());
                            e.setTraitePar(created.getTraitePar());
                            e.setDateDemande(created.getDateDemande());
                            e.setDateTraitement(created.getDateTraitement());
                            e.setDateLivraison(created.getDateLivraison());
                            e.setSyncStatus(0); // synced
                            ma.ensate.myapplication.db.AppDatabase.getInstance(requireContext()).besoinDao().insert(e);
                        });
                        Toast.makeText(requireContext(), "Besoin créé", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).popBackStack();
                    }

                    @Override
                    public void onError(Throwable t) {
                        // Network failed: save locally as pending and schedule sync
                        ma.ensate.myapplication.repository.local.BesoinLocalRepository localRepo = new ma.ensate.myapplication.repository.local.BesoinLocalRepository(requireContext());
                        localRepo.insertPendingFromModel(b);
                        ma.ensate.myapplication.worker.SyncManager.enqueueSync(requireContext());
                        String errorMsg = "Sauvegardé hors-ligne (en attente de synchronisation)";
                        if (t != null) {
                            String errorDetail = t.getMessage();
                            if (errorDetail != null && !errorDetail.isEmpty()) {
                                errorMsg = "Erreur réseau: " + errorDetail + " - " + errorMsg;
                            }
                        }
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                        t.printStackTrace(); // Log to logcat for debugging
                        Navigation.findNavController(view).popBackStack();
                    }
                });
            } else {
                // update - set the ID for the update
                b.setId(editId);
                // For updates, we can also save locally if network fails (optional enhancement)
                viewModel.updateBesoin(editId, b, new BesoinViewModel.ActionCallback() {
                    @Override
                    public void onSuccess(Besoin updated) {
                        Toast.makeText(requireContext(), "Besoin mis à jour", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).popBackStack();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(requireContext(), "Erreur mise à jour: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}