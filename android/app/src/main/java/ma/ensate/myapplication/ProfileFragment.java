package ma.ensate.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE";

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvRole = view.findViewById(R.id.tvRole);
        TextView tvInitials = view.findViewById(R.id.tvInitials);

        View btnEditProfile = view.findViewById(R.id.btnEditProfile);
        View cardLogout = view.findViewById(R.id.cardLogout);

        TextInputEditText etOld = view.findViewById(R.id.etOldPassword);
        TextInputEditText etNew = view.findViewById(R.id.etNewPassword);
        TextInputEditText etConf = view.findViewById(R.id.etConfirmPassword);
        View btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);

        TokenManager tm = new TokenManager(requireContext());
        Long userId = tm.getUserId();

        String username = tm.getUsername();
        String email = tm.getEmail();
        String role = tm.getRole();

        Log.d(TAG, "USER ID=" + userId);
        tm.logUserData();

        tvUsername.setText(username);
        tvEmail.setText(email);
        tvRole.setText(role);

        // Initiale dans le cercle
        if (username != null && !username.trim().isEmpty()) {
            tvInitials.setText(username.trim().substring(0, 1).toUpperCase());
        } else {
            tvInitials.setText("?");
        }

        ProfileViewModel vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        vm.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        // ✅ CLICK SUR LE CRAYON -> Dialog pour changer username
        btnEditProfile.setOnClickListener(v -> {
            Log.d(TAG, "CLICK EDIT PROFILE");

            if (userId == null) {
                Toast.makeText(requireContext(), "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
                return;
            }

            View dialogView = getLayoutInflater()
                    .inflate(R.layout.dialog_edit_username, null);

            TextInputEditText etUsername = dialogView.findViewById(R.id.etUsername);
            etUsername.setText(tvUsername.getText());

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Profil")
                    .setView(dialogView)
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Enregistrer", (dialog, which) -> {
                        String newUsername = etUsername.getText() != null
                                ? etUsername.getText().toString().trim()
                                : "";

                        if (newUsername.isEmpty()) {
                            Toast.makeText(requireContext(),
                                    "Le username ne peut pas être vide",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 🔁 API
                        vm.updateProfile(userId, newUsername);

                        // ⚡ Update UI immédiate
                        tm.setUsername(newUsername);
                        tvUsername.setText(newUsername);
                        tvInitials.setText(newUsername.substring(0, 1).toUpperCase());
                    })
                    .show();
        });

        // ✅ Change password
        btnUpdatePassword.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(requireContext(), "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
                return;
            }

            String oldP = etOld.getText() != null ? etOld.getText().toString().trim() : "";
            String newP = etNew.getText() != null ? etNew.getText().toString().trim() : "";
            String confP = etConf.getText() != null ? etConf.getText().toString().trim() : "";

            if (oldP.isEmpty() || newP.isEmpty() || confP.isEmpty()) {
                Toast.makeText(requireContext(), "Tous les champs sont requis", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newP.equals(confP)) {
                Toast.makeText(requireContext(), "Confirmation incorrecte", Toast.LENGTH_SHORT).show();
                return;
            }

            vm.changePassword(userId, oldP, newP);
        });

        // ✅ Logout
        cardLogout.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            }
        });
    }
}
