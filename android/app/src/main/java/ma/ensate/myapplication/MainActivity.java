package ma.ensate.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationView navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Configure AppBarConfiguration with drawer layout
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment,
                    R.id.personnelFragment,
                    R.id.demandesFragment,
                    R.id.recrutementFragment,
                    R.id.notificationsFragment,
                    R.id.loginFragment
            )
                    .setOpenableLayout(drawerLayout)
                    .build();

            // Setup bottom navigation
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            
            // Setup drawer navigation
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = androidx.navigation.Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
