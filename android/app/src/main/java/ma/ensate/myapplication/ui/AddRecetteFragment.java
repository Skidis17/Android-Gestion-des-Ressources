package ma.ensate.myapplication.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Recette;
import ma.ensate.myapplication.viewmodel.RecetteViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRecetteFragment extends Fragment {

    public AddRecetteFragment() { super(R.layout.fragment_add_recette); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etSource = view.findViewById(R.id.etSource);
        Spinner spinnerCat = view.findViewById(R.id.spinnerCategorie);
        EditText etMontant = view.findViewById(R.id.etMontant);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etReference = view.findViewById(R.id.etReference);
        Button btnSave = view.findViewById(R.id.btnSaveRecette);

        String[] cats = new String[]{"Subvention","Formation Continue","Partenariat","Autre"};
        spinnerCat.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cats));

        RecetteViewModel vm = new ViewModelProvider(this).get(RecetteViewModel.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final Calendar cal = Calendar.getInstance();

        etDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
                cal.set(y, m, d);
                etDate.setText(sdf.format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> {
            // simple validation
            if (etSource.getText().toString().trim().isEmpty() || etMontant.getText().toString().trim().isEmpty() || etDate.getText().toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            Recette r = new Recette();
            r.source = etSource.getText().toString().trim();
            r.categorie = spinnerCat.getSelectedItem().toString();
            try { r.montant = Double.parseDouble(etMontant.getText().toString().trim()); } catch (Exception ex) { r.montant = 0.0; }
            r.date = etDate.getText().toString().trim();
            r.description = etDescription.getText().toString().trim();
            r.reference = etReference.getText().toString().trim();

            vm.createRecette(r, new RecetteViewModel.ActionCallback() {
                @Override
                public void onSuccess(Recette created) {
                    Toast.makeText(requireContext(), "Recette ajoutée", Toast.LENGTH_SHORT).show();
                    androidx.navigation.Navigation.findNavController(view).navigateUp();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}