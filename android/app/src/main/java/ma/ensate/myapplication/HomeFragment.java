package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnGoLogin = view.findViewById(R.id.btnGoLogin);
        ImageView btnMenu = view.findViewById(R.id.btn_menu);

        btnGoLogin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.loginFragment);
        });

        // Open drawer when hamburger menu is clicked
        btnMenu.setOnClickListener(v -> {
            if (getActivity() != null) {
                DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
                if (drawerLayout != null) {
                    drawerLayout.open();
                }
            }
        });
    }
}
