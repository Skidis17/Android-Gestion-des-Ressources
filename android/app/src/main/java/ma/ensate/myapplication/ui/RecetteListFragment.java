package ma.ensate.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Recette;
import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.RecetteViewModel;

import java.text.DecimalFormat;
import java.util.List;

public class RecetteListFragment extends Fragment {

    public RecetteListFragment() {
        super(R.layout.fragment_recettes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etSearch = view.findViewById(R.id.etSearchRecettes);
        RecyclerView rv = view.findViewById(R.id.rvRecettes);
        ProgressBar loading = view.findViewById(R.id.loadingRecettes);
        FloatingActionButton fab = view.findViewById(R.id.fabAddRecette);

        RecetteViewModel vm = new ViewModelProvider(this).get(RecetteViewModel.class);
        DecimalFormat df = new DecimalFormat("#,###.00");

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        RecetteAdapter adapter = new RecetteAdapter();
        rv.setAdapter(adapter);

        // Check user role for permissions
        TokenManager tokenManager = new TokenManager(requireContext());
        String role = tokenManager.getRole();
        boolean isDirecteur = "directeur".equalsIgnoreCase(role);
        boolean canEdit = "directeur".equalsIgnoreCase(role) || "secretaire_general".equalsIgnoreCase(role);
        adapter.setDirecteur(isDirecteur);

        // Hide FAB for Directeur_adjoint (read-only)
        if (!canEdit) {
            fab.setVisibility(View.GONE);
        }

        adapter.setOnRecetteClickListener(recette -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("recette", recette);
            androidx.navigation.Navigation.findNavController(view).navigate(R.id.recetteDetailFragment, bundle);
        });

        adapter.setOnRecetteDeleteListener(recette -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Supprimer la recette")
                    .setMessage("Voulez-vous vraiment supprimer cette recette ?")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        vm.deleteRecette(recette.getId(), new RecetteViewModel.DeleteCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(requireContext(), "Recette supprimée", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        vm.getRecettes().observe(getViewLifecycleOwner(), (List<Recette> list) -> {
            adapter.setItems(list, df);
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading)
                loading.setVisibility(View.VISIBLE);
            else
                loading.setVisibility(View.GONE);
        });

        fab.setOnClickListener(
                v -> androidx.navigation.Navigation.findNavController(view).navigate(R.id.addRecetteFragment));

        // search filtering
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // initial load
        vm.loadRecettes();
    }
}