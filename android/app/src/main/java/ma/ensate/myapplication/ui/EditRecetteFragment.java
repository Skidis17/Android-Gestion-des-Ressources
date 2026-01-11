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

public class EditRecetteFragment extends Fragment {

    private Recette recette;

    public EditRecetteFragment() { super(R.layout.fragment_edit_recette); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get recette from arguments
        if (getArguments() != null) {
            recette = (Recette) getArguments().getSerializable("recette");
        }

        if (recette == null) {
            Toast.makeText(requireContext(), "Erreur: Recette non trouvée", Toast.LENGTH_SHORT).show();
            androidx.navigation.Navigation.findNavController(view).navigateUp();
            return;
        }

        EditText etSource = view.findViewById(R.id.etSource);
        Spinner spinnerCat = view.findViewById(R.id.spinnerCategorie);
        EditText etMontant = view.findViewById(R.id.etMontant);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etReference = view.findViewById(R.id.etReference);
        Button btnUpdate = view.findViewById(R.id.btnUpdateRecette);

        String[] cats = new String[]{"Subvention","Formation Continue","Partenariat","Autre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cats);
        spinnerCat.setAdapter(adapter);

        // Pre-fill form with recette data
        etSource.setText(recette.getSource() != null ? recette.getSource() : "");
        etMontant.setText(recette.getMontant() != null ? String.valueOf(recette.getMontant()) : "");
        etDate.setText(recette.getDateRecette() != null ? recette.getDateRecette() : "");
        etDescription.setText(recette.getDescription() != null ? recette.getDescription() : "");
        etReference.setText(recette.getReferenceDocument() != null ? recette.getReferenceDocument() : "");

        // Set spinner selection
        if (recette.getCategorie() != null) {
            for (int i = 0; i < cats.length; i++) {
                if (cats[i].equals(recette.getCategorie())) {
                    spinnerCat.setSelection(i);
                    break;
                }
            }
        }

        RecetteViewModel vm = new ViewModelProvider(this).get(RecetteViewModel.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final Calendar cal = Calendar.getInstance();

        etDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
                cal.set(y, m, d);
                etDate.setText(sdf.format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnUpdate.setOnClickListener(v -> {
            // Simple validation
            if (etSource.getText().toString().trim().isEmpty() || etMontant.getText().toString().trim().isEmpty() || etDate.getText().toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update recette object
            recette.setSource(etSource.getText().toString().trim());
            recette.setCategorie(spinnerCat.getSelectedItem().toString());
            try { 
                recette.setMontant(Double.parseDouble(etMontant.getText().toString().trim())); 
            } catch (Exception ex) { 
                recette.setMontant(0.0); 
            }
            recette.setDateRecette(etDate.getText().toString().trim());
            recette.setDescription(etDescription.getText().toString().trim());
            recette.setReferenceDocument(etReference.getText().toString().trim());

            vm.updateRecette(recette, new RecetteViewModel.ActionCallback() {
                @Override
                public void onSuccess(Recette updated) {
                    Toast.makeText(requireContext(), "Recette mise à jour", Toast.LENGTH_SHORT).show();
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
