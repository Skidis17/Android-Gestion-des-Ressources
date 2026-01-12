package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnMenu = view.findViewById(R.id.btn_menu);

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() != null) {
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
                    if (drawerLayout != null) drawerLayout.open();
                }
            });
        }

        // Navigate to Budget screen when budget card is clicked
        View cardBudget = view.findViewById(R.id.card_budget);
        if (cardBudget != null) {
            cardBudget.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.budgetFragment);

            });
        }
    }
}
