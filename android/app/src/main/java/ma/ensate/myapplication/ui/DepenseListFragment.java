package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.adapter.DepenseAdapter;
import ma.ensate.myapplication.viewmodel.DepenseViewModel;

public class DepenseListFragment extends Fragment {

    private DepenseViewModel viewModel;
    private DepenseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_depenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv_depenses);
        adapter = new DepenseAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_depense);
        fab.setOnClickListener(v -> Toast.makeText(requireContext(), "Create Depense (not implemented)", Toast.LENGTH_SHORT).show());

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);
        viewModel.getDepenses().observe(getViewLifecycleOwner(), depenses -> adapter.setItems(depenses));
        viewModel.loadDepenses();
    }
}