package ma.ensate.myapplication;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ma.ensate.myapplication.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {

    private static final String TAG = "LOGIN";
    private boolean isPasswordVisible = false;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        ImageView btnTogglePassword = view.findViewById(R.id.btnTogglePassword);
        View btnLogin = view.findViewById(R.id.btnLogin);

        LoginViewModel vm = new ViewModelProvider(this).get(LoginViewModel.class);

        vm.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        vm.getRole().observe(getViewLifecycleOwner(), role -> {
            if (role == null || role.trim().isEmpty()) return;


            Log.d(TAG, "ROLE=" + role);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateAfterLogin(role.trim());
            }
        });

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                String email = etEmail != null ? etEmail.getText().toString().trim() : "";
                String password = etPassword != null ? etPassword.getText().toString().trim() : "";

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Email et mot de passe requis", Toast.LENGTH_SHORT).show();
                    return;
                }

                vm.login(email, password);
            });
        }

        if (btnTogglePassword != null && etPassword != null) {
            btnTogglePassword.setOnClickListener(v -> {
                isPasswordVisible = !isPasswordVisible;

                if (isPasswordVisible) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btnTogglePassword.setImageResource(R.drawable.ic_eye_off);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnTogglePassword.setImageResource(R.drawable.ic_eye);
                }

                etPassword.setSelection(etPassword.getText().length());
            });
        }
    }
}
