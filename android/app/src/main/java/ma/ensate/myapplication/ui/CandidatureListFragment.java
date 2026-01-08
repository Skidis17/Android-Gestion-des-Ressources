package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ma.ensate.myapplication.adapter.CandidatureAdapter;
import ma.ensate.myapplication.viewmodel.CandidatureRecrutementViewModel;
import androidx.navigation.Navigation;

public class CandidatureListFragment extends Fragment {
    public CandidatureListFragment() {
        super(R.layout.fragment_candidatures);
    }

    private Long recrutementId;
    private CandidatureRecrutementViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidatures, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("recrutementId")) {
            recrutementId = args.getLong("recrutementId");
        }
        if (recrutementId == null) {
            Toast.makeText(requireContext(), "Recrutement manquant", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        RecyclerView rv = view.findViewById(R.id.rv_candidatures);
        TextView tvEmpty = view.findViewById(R.id.tv_empty_candidatures);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_candidature);
        View btnBack = view.findViewById(R.id.btn_back);
        View btnSelect = view.findViewById(R.id.btn_select_candidates);
        com.google.android.material.switchmaterial.SwitchMaterial swSendEmail =
                view.findViewById(R.id.sw_send_email_select);

        CandidatureAdapter adapter = new CandidatureAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);
        adapter.setOnItemClick(c -> {
            if (c.getId() != null) {
                Bundle b = new Bundle();
                b.putLong("candidatureId", c.getId());
                Navigation.findNavController(view).navigate(R.id.candidatureDetailFragment, b);
            }
        });

        viewModel = new ViewModelProvider(this).get(CandidatureRecrutementViewModel.class);
        viewModel.getCandidatures().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
        });
        viewModel.loadByRecrutement(recrutementId);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        }

        if (btnSelect != null) {
            btnSelect.setOnClickListener(v -> {
                boolean sendEmail = swSendEmail != null && swSendEmail.isChecked();
                viewModel.selectAccepted(recrutementId, sendEmail, new CandidatureRecrutementViewModel.ListCallback() {
                    @Override
                    public void onSuccess(java.util.List<ma.ensate.myapplication.model.CandidatureRecrutement> list) {
                        Toast.makeText(requireContext(), "Sélection effectuée", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        fab.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putLong("recrutementId", recrutementId);
            Navigation.findNavController(view).navigate(R.id.candidatureFormFragment, b);
        });
    }
}
