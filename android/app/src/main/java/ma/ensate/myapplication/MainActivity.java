package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ma.ensate.myapplication.network.TokenManager;

public class MainActivity extends AppCompatActivity {

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

        if (navHostFragment == null) return;

        navController = navHostFragment.getNavController();

        // Top-level destinations
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.personnelFragment,
                R.id.demandesFragment,
                R.id.recrutementFragment,
                R.id.notificationsFragment,
                R.id.profileFragment,
                R.id.loginFragment
        ).setOpenableLayout(drawerLayout).build();

        // Connect bottom nav with nav controller (IDs must match nav_graph destinations)
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // ✅ Drawer listener custom (to control profile access)
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.profileFragment) {
                openProfile();
                drawerLayout.closeDrawers();
                return true;
            }

            if (id == R.id.loginFragment) {
                navController.navigate(R.id.action_global_loginFragment);
                drawerLayout.closeDrawers();
                updateUiForAuthState(R.id.loginFragment);
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawerLayout.closeDrawers();
            return handled;
        });

        // ✅ Bottom nav: protect profile too
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.profileFragment) {
                openProfile();
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateUiForAuthState(destination.getId());
        });

        // Initial UI state
        int start = (navController.getCurrentDestination() != null)
                ? navController.getCurrentDestination().getId()
                : R.id.loginFragment;
        updateUiForAuthState(start);
    }

    private boolean isLoggedIn() {
        return new TokenManager(this).getToken() != null;
    }

    private void updateUiForAuthState(int destinationId) {
        boolean loggedIn = isLoggedIn();

        if (!loggedIn || destinationId == R.id.loginFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    // ✅ Called after login to redirect based on role
    public void navigateAfterLogin(String role) {
        new TokenManager(this).logUserData();

        int destId;

        if ("RH".equalsIgnoreCase(role)) {
            destId = R.id.homeFragment;
        } else if ("recruteur".equalsIgnoreCase(role)) {
            destId = R.id.recrutementFragment;
        } else {
            destId = R.id.homeFragment; // fallback
        }

        NavOptions options = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();

        navController.navigate(destId, null, options);
        updateUiForAuthState(destId);
    }

    // ✅ Open profile from anywhere
    public void openProfile() {
        if (!isLoggedIn()) {
            // aller au login depuis n'importe où
            NavOptions opts = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build();

            navController.navigate(R.id.action_global_loginFragment, null, opts);
            updateUiForAuthState(R.id.loginFragment);

        } else {
            // aller au profile depuis n'importe où
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.profileFragment) {
                return; // déjà sur profile
            }

            NavOptions opts = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build();

            navController.navigate(R.id.action_global_profileFragment, null, opts);
            updateUiForAuthState(R.id.profileFragment);
        }
    }


    // ✅ Logout called from profile fragment
    public void logout() {
        new TokenManager(this).clearAuth();

        NavOptions opts = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation, true) // vide la stack
                .build();

        navController.navigate(R.id.action_global_loginFragment, null, opts);
        updateUiForAuthState(R.id.loginFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
