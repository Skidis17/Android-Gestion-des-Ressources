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
import ma.ensate.myapplication.model.Depense;
import ma.ensate.myapplication.viewmodel.DepenseViewModel;

public class DepenseFormFragment extends Fragment {
    private DepenseViewModel viewModel;
    private EditText etBesoinId, etCategorie, etMontant, etDate, etFournisseur, etDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_depense_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etBesoinId = view.findViewById(R.id.et_besoin_id);
        etCategorie = view.findViewById(R.id.et_categorie);
        etMontant = view.findViewById(R.id.et_montant);
        etDate = view.findViewById(R.id.et_date_depense);
        etFournisseur = view.findViewById(R.id.et_fournisseur);
        etDescription = view.findViewById(R.id.et_description);
        Button btn = view.findViewById(R.id.btn_save);

        viewModel = new ViewModelProvider(requireActivity()).get(DepenseViewModel.class);

        btn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etCategorie.getText()) || TextUtils.isEmpty(etMontant.getText())) {
                Toast.makeText(requireContext(), "Catégorie et montant requis", Toast.LENGTH_SHORT).show();
                return;
            }
            Depense d = new Depense();
            if (!TextUtils.isEmpty(etBesoinId.getText())) d.setBesoinId(Long.parseLong(etBesoinId.getText().toString()));
            d.setCategorie(etCategorie.getText().toString());
            String montantStr = etMontant.getText().toString();
            try { d.setMontant(new java.math.BigDecimal(montantStr)); } catch (Exception ex) { d.setMontant(null); }
            d.setDateDepense(etDate.getText().toString());
            d.setFournisseur(etFournisseur.getText().toString());
            d.setDescription(etDescription.getText().toString());

            // Attempt network create first, fallback to local + sync on failure
            viewModel.createDepense(d, new DepenseViewModel.ActionCallback() {
                @Override
                public void onSuccess(ma.ensate.myapplication.model.Depense created) {
                    // Network success: save to local as synced
                    ma.ensate.myapplication.repository.local.DepenseLocalRepository localRepo = new ma.ensate.myapplication.repository.local.DepenseLocalRepository(requireContext());
                    java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                        ma.ensate.myapplication.db.entity.DepenseEntity e = new ma.ensate.myapplication.db.entity.DepenseEntity();
                        e.setServerId(created.getId());
                        e.setBesoinId(created.getBesoinId());
                        e.setCategorie(created.getCategorie());
                        e.setMontant(created.getMontant() != null ? created.getMontant().toPlainString() : null);
                        e.setDateDepense(created.getDateDepense());
                        e.setFournisseur(created.getFournisseur());
                        e.setDescription(created.getDescription());
                        e.setSyncStatus(0); // synced
                        ma.ensate.myapplication.db.AppDatabase.getInstance(requireContext()).depenseDao().insert(e);
                    });
                    Toast.makeText(requireContext(), "Dépense créée", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }

                @Override
                public void onError(Throwable t) {
                    // Network failed: save locally as pending and schedule sync
                    ma.ensate.myapplication.repository.local.DepenseLocalRepository localRepo = new ma.ensate.myapplication.repository.local.DepenseLocalRepository(requireContext());
                    localRepo.insertPendingFromModel(d);
                    ma.ensate.myapplication.worker.SyncManager.enqueueSync(requireContext());
                    Toast.makeText(requireContext(), "Sauvegardé hors-ligne (en attente de synchronisation)", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }
            });
        });
    }
}