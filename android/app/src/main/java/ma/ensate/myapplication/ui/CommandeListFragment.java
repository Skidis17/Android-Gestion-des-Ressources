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
import ma.ensate.myapplication.adapter.CommandeAdapter;
import ma.ensate.myapplication.viewmodel.CommandeViewModel;

public class CommandeListFragment extends Fragment {

    private CommandeViewModel viewModel;
    private CommandeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commandes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv_commandes);
        adapter = new CommandeAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_commande);
        fab.setOnClickListener(v -> Toast.makeText(requireContext(), "Create Commande (not implemented)", Toast.LENGTH_SHORT).show());

        viewModel = new ViewModelProvider(this).get(CommandeViewModel.class);
        viewModel.getCommandes().observe(getViewLifecycleOwner(), commandes -> adapter.setItems(commandes));
        viewModel.loadCommandes();
    }
}