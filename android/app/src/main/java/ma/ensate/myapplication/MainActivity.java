package ma.ensate.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ma.ensate.myapplication.network.TokenManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AUTH";

    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            Log.e(TAG, "NavHostFragment is null");
            return;
        }

        navController = navHostFragment.getNavController();

        // Configure AppBarConfiguration with drawer layout
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.personnelFragment,
                R.id.demandesFragment,
                R.id.recrutementFragment,
                R.id.notificationsFragment,
                R.id.recettesFragment,
                R.id.budgetFragment,
                R.id.loginFragment
        )
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Log.d(TAG, "Destination=" + destination.getId());

            if (destination.getId() == R.id.loginFragment) {
                bottomNavigationView.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            refreshBottomLoginTitle();
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d(TAG, "Bottom click id=" + id + " isLoggedIn=" + isLoggedIn());

            if (id == R.id.loginFragment) {
                navController.navigate(R.id.loginFragment);
                return true;
            }

            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        refreshBottomLoginTitle();
    }

    private boolean isLoggedIn() {
        return new TokenManager(this).getToken() != null;
    }

    public void refreshBottomLoginTitle() {
        if (bottomNavigationView == null) return;

        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.loginFragment);

        Log.d(TAG, "refreshBottomLoginTitle findItem=" + item);

        if (item == null) {
            Log.e(TAG, "Bottom MenuItem loginFragment NOT FOUND (check bottom_navigation_menu.xml id)");
            return;
        }

        String token = new TokenManager(this).getToken();
        Log.d(TAG, "token=" + token);

        if (token != null) {
            item.setTitle("Logout");
            item.setIcon(R.drawable.ic_lock); // ou ic_logout
        } else {
            item.setTitle(getString(R.string.nav_login));
            item.setIcon(R.drawable.ic_lock);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
