package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.app.DatePickerDialog;
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
import ma.ensate.myapplication.model.Recrutement;
import ma.ensate.myapplication.viewmodel.RecrutementViewModel;

import java.util.Calendar;

public class RecrutementFormFragment extends Fragment {

    public RecrutementFormFragment() {
        super(R.layout.fragment_recrutement_form);
    }

    private RecrutementViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recrutement_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RecrutementViewModel.class);

        EditText etPoste = view.findViewById(R.id.et_poste);
        EditText etTypeContrat = view.findViewById(R.id.et_type_contrat);
        EditText etDepartement = view.findViewById(R.id.et_departement);
        EditText etNombrePostes = view.findViewById(R.id.et_nombre_postes);
        EditText etDateOuverture = view.findViewById(R.id.et_date_ouverture);
        EditText etDateCloture = view.findViewById(R.id.et_date_cloture);
        EditText etDescription = view.findViewById(R.id.et_description);
        Button btnSave = view.findViewById(R.id.btn_save_recrutement);
        View btnBack = view.findViewById(R.id.btn_back);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        }

        etDateOuverture.setOnClickListener(v -> showDatePicker(etDateOuverture));
        etDateCloture.setOnClickListener(v -> showDatePicker(etDateCloture));

        btnSave.setOnClickListener(v -> {
            String poste = etPoste.getText().toString().trim();
            String typeContrat = etTypeContrat.getText().toString().trim();
            String departement = etDepartement.getText().toString().trim();
            String nbPostesStr = etNombrePostes.getText().toString().trim();
            String dateOuverture = etDateOuverture.getText().toString().trim();
            String dateCloture = etDateCloture.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();

            if (TextUtils.isEmpty(poste) || TextUtils.isEmpty(typeContrat) || TextUtils.isEmpty(departement)
                    || TextUtils.isEmpty(dateOuverture) || TextUtils.isEmpty(dateCloture)) {
                Toast.makeText(requireContext(), "Champs requis manquants", Toast.LENGTH_SHORT).show();
                return;
            }

            Recrutement r = new Recrutement();
            r.setPoste(poste);
            r.setTypeContrat(typeContrat);
            r.setDepartement(departement);
            r.setNombrePostes(!TextUtils.isEmpty(nbPostesStr) ? Integer.parseInt(nbPostesStr) : 1);
            r.setDateOuverture(dateOuverture);
            r.setDateCloture(dateCloture);
            r.setDescription(desc);
            r.setStatut("OUVERT");

            viewModel.createRecrutement(r, new RecrutementViewModel.ActionCallback() {
                @Override
                public void onSuccess(Recrutement created) {
                    Toast.makeText(requireContext(), "Recrutement créé", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showDatePicker(EditText target) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String m = String.format("%02d", month + 1);
                    String d = String.format("%02d", dayOfMonth);
                    target.setText(year + "-" + m + "-" + d);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
