package ma.ensate.myapplication;

import android.os.Bundle;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);

        ImageView btnTogglePassword = view.findViewById(R.id.btnTogglePassword);
        View btnLogin = view.findViewById(R.id.btnLogin); // ✅ View (TextView dans ton XML)

        LoginViewModel vm = new ViewModelProvider(this).get(LoginViewModel.class);

        vm.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Log.d(TAG, "MESSAGE=" + msg);
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        vm.getRole().observe(getViewLifecycleOwner(), role -> {
            if (role == null) return;

            Log.d(TAG, "ROLE OBSERVED=" + role);

            if ("RH".equalsIgnoreCase(role)) {
                Log.d(TAG, "Redirect -> homeFragment");
                Navigation.findNavController(view).navigate(R.id.homeFragment);

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).refreshBottomLoginTitle();
                }
            }else if ("recruteur".equalsIgnoreCase(role)) {
                    Log.d(TAG, "Redirect -> recrutementFragment");
                    Navigation.findNavController(view).navigate(R.id.recrutementFragment);

                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).refreshBottomLoginTitle();
                    }

            } else {
                Toast.makeText(requireContext(),
                        "Connecté avec le rôle : " + role,
                        Toast.LENGTH_LONG).show();
            }
        });

        // ✅ click login
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                String email = etEmail != null ? etEmail.getText().toString().trim() : "";
                String password = etPassword != null ? etPassword.getText().toString().trim() : "";

                Log.d(TAG, "CLICK LOGIN email=" + email);

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
