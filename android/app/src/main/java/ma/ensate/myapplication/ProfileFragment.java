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
        super(R.layout.fragment_profile);
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

        Log.d("TEST", "USER ID = " + tm.getUserId());
        Log.d("PROFILE", "username=" + username);
        Log.d("PROFILE", "email=" + email);
        Log.d("PROFILE", "role=" + role);


        tvUsername.setText(username != null ? username : "—");
        tvEmail.setText(email != null ? email : "—");
        tvRole.setText(role != null ? role : "—");

        // ✅ LOGOUT CLICK
        View cardLogout = view.findViewById(R.id.cardLogout);
        TextView btnLogout = view.findViewById(R.id.btnLogout);

        Log.d("PROFILE", "cardLogout=" + cardLogout + " btnLogout=" + btnLogout);

        cardLogout.setOnClickListener(v -> {
            Log.d("PROFILE", "LOGOUT CARD CLICKED");

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            } else {
                Log.e("PROFILE", "Activity is not MainActivity");
            }
        });

    }


}
