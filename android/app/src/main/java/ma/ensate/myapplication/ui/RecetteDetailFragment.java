package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Recette;

public class RecetteDetailFragment extends Fragment {

    private Recette recette;
    private TextView tvDetailSource, tvDetailMontant, tvDetailCategorie;
    private TextView tvDetailDate, tvDetailDescription, tvDetailReference;
    private Button btnEditRecette;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recette_detail, container, false);

        // Initialize views
        tvDetailSource = view.findViewById(R.id.tvDetailSource);
        tvDetailMontant = view.findViewById(R.id.tvDetailMontant);
        tvDetailCategorie = view.findViewById(R.id.tvDetailCategorie);
        tvDetailDate = view.findViewById(R.id.tvDetailDate);
        tvDetailDescription = view.findViewById(R.id.tvDetailDescription);
        tvDetailReference = view.findViewById(R.id.tvDetailReference);
        btnEditRecette = view.findViewById(R.id.btnEditRecette);

        // Get recette from arguments
        if (getArguments() != null) {
            recette = (Recette) getArguments().getSerializable("recette");
            if (recette != null) {
                displayRecetteDetails();
            }
        }

        // Edit button click
        btnEditRecette.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("recette", recette);
            Navigation.findNavController(v).navigate(R.id.action_recetteDetailFragment_to_editRecetteFragment, bundle);
        });

        return view;
    }

    private void displayRecetteDetails() {
        tvDetailSource.setText(recette.getSource());

        // Format montant
        DecimalFormat df = new DecimalFormat("#,##0.00");
        tvDetailMontant.setText(df.format(recette.getMontant()) + " DH");

        tvDetailCategorie.setText(recette.getCategorie() != null ? recette.getCategorie() : "N/A");

        // Date is already a String from the API
        tvDetailDate.setText(recette.getDateRecette() != null ? recette.getDateRecette() : "N/A");

        tvDetailDescription.setText(recette.getDescription() != null && !recette.getDescription().isEmpty()
                ? recette.getDescription()
                : "Aucune description");

        tvDetailReference.setText(recette.getReferenceDocument() != null && !recette.getReferenceDocument().isEmpty()
                ? recette.getReferenceDocument()
                : "N/A");
    }
}
