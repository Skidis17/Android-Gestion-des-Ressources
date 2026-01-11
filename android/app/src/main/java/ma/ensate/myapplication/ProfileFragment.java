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

import com.google.android.material.textfield.TextInputEditText;

import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE";

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvRole = view.findViewById(R.id.tvRole);

        TextInputEditText etOld = view.findViewById(R.id.etOldPassword);
        TextInputEditText etNew = view.findViewById(R.id.etNewPassword);
        TextInputEditText etConf = view.findViewById(R.id.etConfirmPassword);
        View btnUpdate = view.findViewById(R.id.btnUpdatePassword);
        View cardLogout = view.findViewById(R.id.cardLogout);

        TokenManager tm = new TokenManager(requireContext());
        Long userId = tm.getUserId();

        Log.d(TAG, "USER ID=" + userId);
        tm.logUserData();

        tvUsername.setText(tm.getUsername());
        tvEmail.setText(tm.getEmail());
        tvRole.setText(tm.getRole());

        ProfileViewModel vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        vm.getMessage().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        );

        btnUpdate.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(requireContext(), "Utilisateur non identifié", Toast.LENGTH_SHORT).show();
                return;
            }

            String oldP = etOld.getText().toString().trim();
            String newP = etNew.getText().toString().trim();
            String confP = etConf.getText().toString().trim();

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

        cardLogout.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            }
        });
    }
}
