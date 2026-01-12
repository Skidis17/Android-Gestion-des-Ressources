package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ma.ensate.myapplication.model.AddUserRequest;
import ma.ensate.myapplication.model.AddUserResponse;
import ma.ensate.myapplication.model.PersonnelOption;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserBottomSheet extends BottomSheetDialogFragment {

    public interface OnUserAddedListener {
        void onUserAdded();
    }

    private OnUserAddedListener listener;
    public void setListener(OnUserAddedListener listener) {
        this.listener = listener;
    }

    private ApiService api;

    private AutoCompleteTextView spPersonnelId;
    private AutoCompleteTextView spRole;
    private TextInputEditText etEmail, etUsername, etPassword;
    private MaterialButton btnCancel, btnAdd;

    private final List<PersonnelOption> personnels = new ArrayList<>();
    private ArrayAdapter<String> personnelAdapter;

    public AddUserBottomSheet() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_add_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api = RetrofitClient.api();

        spPersonnelId = view.findViewById(R.id.spPersonnelId);
        spRole = view.findViewById(R.id.spRole);
        etEmail = view.findViewById(R.id.etEmail);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnAdd = view.findViewById(R.id.btnAdd);

        setupRoles();
        loadPersonnelDropdown();

        btnCancel.setOnClickListener(v -> dismiss());
        btnAdd.setOnClickListener(v -> submit());
    }

    // ✅ construit "Bearer <token>" ou retourne null si pas loggé
    private String authHeader() {
        String token = new TokenManager(requireContext()).getToken();
        if (token == null || token.trim().isEmpty()) return null;
        return "Bearer " + token;
    }

    private void setupRoles() {
        // ✅ valeurs match DB / backend
        List<String> roles = Arrays.asList(
                "admin",
                "RH",
                "recruteur",
                "directeur",
                "secretaire_general",
                "Directeur_adjoint"
        );

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                roles
        );

        spRole.setAdapter(roleAdapter);
        spRole.setText("RH", false);
    }

    private void loadPersonnelDropdown() {
        String auth = authHeader();
        if (auth == null) {
            Toast.makeText(requireContext(), "Connecte-toi d'abord", Toast.LENGTH_SHORT).show();
            return;
        }

        api.getAllPersonnels(auth).enqueue(new Callback<List<PersonnelOption>>() {
            @Override
            public void onResponse(Call<List<PersonnelOption>> call, Response<List<PersonnelOption>> response) {

                if (!response.isSuccessful()) {
                    String err = "Erreur chargement personnel (" + response.code() + ")";
                    try {
                        if (response.errorBody() != null) err += " : " + response.errorBody().string();
                    } catch (Exception ignored) {}
                    Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show();
                    return;
                }

                List<PersonnelOption> body = response.body();
                if (body == null) {
                    Toast.makeText(requireContext(), "Aucun personnel (body null)", Toast.LENGTH_SHORT).show();
                    return;
                }

                personnels.clear();
                personnels.addAll(body);

                List<String> labels = new ArrayList<>();
                for (PersonnelOption p : personnels) {
                    if (p.getLabel() != null && !p.getLabel().trim().isEmpty()) {
                        labels.add(p.getLabel().trim());
                    }
                }

                personnelAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        labels
                );

                spPersonnelId.setAdapter(personnelAdapter);

                // ✅ Afficher la liste dès le clic (même sans taper)
                spPersonnelId.setThreshold(0);
                spPersonnelId.setOnClickListener(v -> spPersonnelId.showDropDown());

                spPersonnelId.setOnItemClickListener((parent, v, position, id) -> {
                    String selected = (String) parent.getItemAtPosition(position);
                    PersonnelOption p = findByLabel(selected);
                    if (p != null) {
                        etEmail.setText(p.getEmail() != null ? p.getEmail() : "");
                    }
                });

                if (labels.isEmpty()) {
                    Toast.makeText(requireContext(), "Liste personnel vide", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PersonnelOption>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur réseau personnel: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private PersonnelOption findByLabel(String label) {
        for (PersonnelOption p : personnels) {
            if (p.getLabel() != null && p.getLabel().equals(label)) return p;
        }
        return null;
    }

    private void submit() {
        String selectedPersonnel = spPersonnelId.getText() != null ? spPersonnelId.getText().toString().trim() : "";
        PersonnelOption p = findByLabel(selectedPersonnel);

        if (p == null || p.getId() == null) {
            Toast.makeText(requireContext(), "Choisis un personnel", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = spRole.getText() != null ? spRole.getText().toString().trim() : "";
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (username.isEmpty()) {
            Toast.makeText(requireContext(), "Username requis", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Mot de passe requis", Toast.LENGTH_SHORT).show();
            return;
        }
        if (role.isEmpty()) {
            Toast.makeText(requireContext(), "Rôle requis", Toast.LENGTH_SHORT).show();
            return;
        }

        String auth = authHeader();
        if (auth == null) {
            Toast.makeText(requireContext(), "Connecte-toi d'abord", Toast.LENGTH_SHORT).show();
            return;
        }

        AddUserRequest req = new AddUserRequest(
                p.getId(),
                username,
                password,
                role
        );

        btnAdd.setEnabled(false);

        api.addUser(auth, req).enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {
                btnAdd.setEnabled(true);

                if (!response.isSuccessful()) {
                    Toast.makeText(requireContext(),
                            "Erreur ajout (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(requireContext(), "Utilisateur ajouté", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onUserAdded();
                dismiss();
            }

            @Override
            public void onFailure(Call<AddUserResponse> call, Throwable t) {
                btnAdd.setEnabled(true);
                Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
