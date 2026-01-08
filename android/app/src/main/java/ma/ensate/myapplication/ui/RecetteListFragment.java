package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Recette;
import ma.ensate.myapplication.viewmodel.RecetteViewModel;

import java.text.DecimalFormat;
import java.util.List;

public class RecetteListFragment extends Fragment {

    public RecetteListFragment() { super(R.layout.fragment_recettes); }

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

        vm.getRecettes().observe(getViewLifecycleOwner(), (List<Recette> list) -> {
            adapter.setItems(list, df);
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) loading.setVisibility(View.VISIBLE); else loading.setVisibility(View.GONE);
        });

        fab.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(view).navigate(R.id.addRecetteFragment));

        // initial load
        vm.loadRecettes();
    }
}