package ma.ensate.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ma.ensate.myapplication.network.TokenManager;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile); // ton xml profile
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvRole = view.findViewById(R.id.tvRole);

        TokenManager tm = new TokenManager(requireContext());

        String username = tm.getUsername();
        String email = tm.getEmail();
        String role = tm.getRole();

        Log.d("PROFILE", "username=" + username);
        Log.d("PROFILE", "email=" + email);
        Log.d("PROFILE", "role=" + role);

        tvUsername.setText(username != null ? username : "—");
        tvEmail.setText(email != null ? email : "—");
        tvRole.setText(role != null ? role : "—");
    }

}
