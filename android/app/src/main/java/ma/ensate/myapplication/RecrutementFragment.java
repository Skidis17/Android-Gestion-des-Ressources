package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.Navigation;

import ma.ensate.myapplication.adapter.RecrutementAdapter;
import ma.ensate.myapplication.viewmodel.RecrutementViewModel;

public class RecrutementFragment extends Fragment {
    public RecrutementFragment() {
        super(R.layout.fragment_recrutement);
    }

    private RecrutementViewModel viewModel;
    private RecrutementAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recrutement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_recrutements);
        TextView tvEmpty = view.findViewById(R.id.tv_empty_state);
        View btnAdd = view.findViewById(R.id.btn_add_recrutement);
        TextView tvCountRecrutements = view.findViewById(R.id.tv_count_recrutements);
        TextView tvCountCandidatures = view.findViewById(R.id.tv_count_candidatures);
        TextView tvCountEntretiens = view.findViewById(R.id.tv_count_entretiens);

        adapter = new RecrutementAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);
        adapter.setOnItemClick(r -> {
            if (r.getId() != null) {
                Bundle args = new Bundle();
                args.putLong("recrutementId", r.getId());
                Navigation.findNavController(view).navigate(R.id.candidatureListFragment, args);
            }
        });

        viewModel = new ViewModelProvider(this).get(RecrutementViewModel.class);
        viewModel.getRecrutements().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
            int count = list != null ? list.size() : 0;
            if (tvCountRecrutements.getText().toString().isEmpty()) {
                tvCountRecrutements.setText(String.valueOf(count));
            }
        });

        viewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats == null) return;
            tvCountRecrutements.setText(String.valueOf(stats.getPostesOuverts()));
            tvCountCandidatures.setText(String.valueOf(stats.getTotalCandidatures()));
            if (tvCountEntretiens != null) {
                tvCountEntretiens.setText(String.valueOf(stats.getEntretiensPlanifies()));
            }
        });

        viewModel.loadRecrutements();
        viewModel.loadStats();

        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.recrutementFormFragment));
        }
    }
}
