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
import ma.ensate.myapplication.model.Commande;
import ma.ensate.myapplication.viewmodel.CommandeViewModel;

public class CommandeFormFragment extends Fragment {
    private CommandeViewModel viewModel;
    private EditText etBesoinId, etFournisseur, etMontant, etDateCommande, etNotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commande_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etBesoinId = view.findViewById(R.id.et_besoin_id);
        etFournisseur = view.findViewById(R.id.et_fournisseur);
        etMontant = view.findViewById(R.id.et_montant);
        etDateCommande = view.findViewById(R.id.et_date_commande);
        etNotes = view.findViewById(R.id.et_notes);
        Button btn = view.findViewById(R.id.btn_save);

        viewModel = new ViewModelProvider(requireActivity()).get(CommandeViewModel.class);

        btn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etFournisseur.getText()) || TextUtils.isEmpty(etMontant.getText())) {
                Toast.makeText(requireContext(), "Fournisseur et montant requis", Toast.LENGTH_SHORT).show();
                return;
            }

            Commande c = new Commande();
            final Long besoinId = !TextUtils.isEmpty(etBesoinId.getText()) ? Long.parseLong(etBesoinId.getText().toString()) : null;
            c.setBesoinId(besoinId);
            c.setFournisseur(etFournisseur.getText().toString());
            final String montantStr = etMontant.getText().toString();
            try { c.setMontantTotal(new java.math.BigDecimal(montantStr)); } catch (Exception ex) { c.setMontantTotal(null); }
            c.setDateCommande(etDateCommande.getText().toString());
            c.setNotes(etNotes.getText().toString());

            // Attempt network create first, fallback to local + sync on failure
            viewModel.createCommande(c, new CommandeViewModel.ActionCallback() {
                @Override
                public void onSuccess(ma.ensate.myapplication.model.Commande created) {
                    // Network success: save to local as synced
                    ma.ensate.myapplication.repository.local.CommandeLocalRepository localRepo = new ma.ensate.myapplication.repository.local.CommandeLocalRepository(requireContext());
                    java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                        ma.ensate.myapplication.db.entity.CommandeEntity e = new ma.ensate.myapplication.db.entity.CommandeEntity();
                        e.setServerId(created.getId());
                        e.setBesoinId(created.getBesoinId());
                        e.setFournisseur(created.getFournisseur());
                        e.setMontantTotal(created.getMontantTotal() != null ? created.getMontantTotal().toPlainString() : null);
                        e.setDateCommande(created.getDateCommande());
                        e.setNotes(created.getNotes());
                        e.setSyncStatus(0); // synced
                        ma.ensate.myapplication.db.AppDatabase.getInstance(requireContext()).commandeDao().insert(e);
                    });
                    Toast.makeText(requireContext(), "Commande créée", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }

                @Override
                public void onError(Throwable t) {
                    // Network failed: save locally as pending and schedule sync
                    ma.ensate.myapplication.repository.local.CommandeLocalRepository localRepo = new ma.ensate.myapplication.repository.local.CommandeLocalRepository(requireContext());
                    localRepo.insertPendingFromFields(besoinId, c.getFournisseur(), montantStr, c.getDateCommande(), c.getNotes());
                    ma.ensate.myapplication.worker.SyncManager.enqueueSync(requireContext());
                    Toast.makeText(requireContext(), "Sauvegardé hors-ligne (en attente de synchronisation)", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }
            });
        });
    }
}