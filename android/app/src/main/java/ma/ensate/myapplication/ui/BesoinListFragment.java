package ma.ensate.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.adapter.BesoinAdapter;
import ma.ensate.myapplication.model.Besoin;
import ma.ensate.myapplication.viewmodel.BesoinViewModel;

public class BesoinListFragment extends Fragment {

    private BesoinViewModel viewModel;
    private BesoinAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_besoins, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv_besoins);
        adapter = new BesoinAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_besoin);
        fab.setOnClickListener(v -> {
            // Navigate to create form (no besoinId argument - will be null)
            Navigation.findNavController(view).navigate(R.id.besoinFormFragment);
        });

        viewModel = new ViewModelProvider(this).get(BesoinViewModel.class);
        viewModel.getBesoins().observe(getViewLifecycleOwner(), besoins -> adapter.setItems(besoins));
        
        // Set up adapter listeners
        adapter.setOnItemClickListener(besoin -> {
            // Navigate to detail view
            Bundle args = new Bundle();
            if (besoin.getId() != null) {
                args.putInt("besoinId", besoin.getId().intValue());
            }
            Navigation.findNavController(view).navigate(R.id.besoinDetailFragment, args);
        });

        adapter.setOnEditClickListener(besoin -> {
            // Only allow edit if status is EN_ATTENTE
            if (!"EN_ATTENTE".equals(besoin.getStatut())) {
                Toast.makeText(requireContext(), "Le besoin ne peut être modifié qu'en statut EN_ATTENTE", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle args = new Bundle();
            if (besoin.getId() != null) {
                args.putInt("besoinId", besoin.getId().intValue());
            }
            Navigation.findNavController(view).navigate(R.id.besoinFormFragment, args);
        });

        adapter.setOnDeleteClickListener(besoin -> {
            // Only allow delete if status is EN_ATTENTE
            if (!"EN_ATTENTE".equals(besoin.getStatut())) {
                Toast.makeText(requireContext(), "Le besoin ne peut être supprimé qu'en statut EN_ATTENTE", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(requireContext())
                    .setTitle("Supprimer le besoin")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce besoin ?")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        viewModel.deleteBesoin(besoin.getId(), new BesoinViewModel.ActionCallback() {
                            @Override
                            public void onSuccess(Besoin deleted) {
                                Toast.makeText(requireContext(), "Besoin supprimé", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Erreur lors de la suppression: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        viewModel.loadBesoins();
    }
}