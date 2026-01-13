package ma.ensate.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.TokenManager;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private String currentRole = null; // admin / RH / recruteur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RetrofitClient with application context
        RetrofitClient.init(getApplicationContext());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            Log.e("MAIN", "NavHostFragment introuvable");
            return;
        }

        navController = navHostFragment.getNavController();

        // AppBar / Drawer (destinations principales)
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.personnelFragment,
                R.id.demandesFragment,
                R.id.recrutementFragment,
                R.id.notificationsFragment,
                R.id.profileFragment,
                R.id.adminUsersFragment,
                // si tu as vraiment ces fragments dans ton graph, tu peux les garder:
                R.id.recettesFragment,
                R.id.budgetFragment,
                R.id.loginFragment
        ).setOpenableLayout(drawerLayout).build();

        // Drawer ↔ NavController
        NavigationUI.setupWithNavController(navigationView, navController);

        // Bottom nav ↔ NavController (menu par défaut)
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Drawer custom (profile / login)
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.profileFragment) {
                openProfile();
                drawerLayout.closeDrawers();
                return true;
            }

            if (id == R.id.loginFragment) {
                goToLoginAndClearBackStack();
                drawerLayout.closeDrawers();
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled)
                drawerLayout.closeDrawers();
            return handled;
        });

        // Bottom nav (protéger profile)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profileFragment) {
                openProfile();
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        // UI selon destination
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateUiForDestination(destination.getId());
        });

        // Etat initial
        int start = (navController.getCurrentDestination() != null)
                ? navController.getCurrentDestination().getId()
                : R.id.loginFragment;

        updateUiForDestination(start);
    }

    /* ===================== AUTH / ROLE ===================== */

    private boolean isLoggedIn() {
        return new TokenManager(this).getToken() != null;
    }

    private void applyBottomMenuForRole(String role) {
        if (bottomNavigationView == null)
            return;

        bottomNavigationView.getMenu().clear();

        if ("admin".equalsIgnoreCase(role)) {
            bottomNavigationView.inflateMenu(R.menu.bottom_admin_navigation);
        } else {
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);
        }

        // IMPORTANT : re-lier après changement de menu
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    // Tu avais un nom "refreshBottomLoginTitle" : je le garde, mais corrigé
    public void refreshBottomLoginTitle() {
        applyBottomMenuForRole(currentRole);
    }

    /* ===================== UI ===================== */

    private void updateUiForDestination(@IdRes int destinationId) {
        if (!isLoggedIn() || destinationId == R.id.loginFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            return;
        }

        bottomNavigationView.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /* ===================== NAVIGATION ===================== */

    // Appelée après login
    public void navigateAfterLogin(String role) {
        currentRole = role;
        applyBottomMenuForRole(role);

        int destId;
        if ("admin".equalsIgnoreCase(role)) {
            destId = R.id.adminUsersFragment;
        } else if ("recruteur".equalsIgnoreCase(role)) {
            destId = R.id.recrutementFragment;
        } else {
            destId = R.id.homeFragment; // RH / autre
        }

        NavOptions options = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .setLaunchSingleTop(true)
                .build();

        navController.navigate(destId, null, options);
        updateUiForDestination(destId);
    }

    public void openProfile() {
        if (!isLoggedIn()) {
            goToLoginAndClearBackStack();
            return;
        }

        if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() == R.id.profileFragment) {
            return;
        }

        navController.navigate(R.id.profileFragment);
        updateUiForDestination(R.id.profileFragment);
    }

    private void goToLoginAndClearBackStack() {
        currentRole = null;

        // menu par défaut
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        NavOptions options = new NavOptions.Builder()
                .setPopUpTo(navController.getGraph().getStartDestinationId(), true)
                .setLaunchSingleTop(true)
                .build();

        navController.navigate(R.id.loginFragment, null, options);
        updateUiForDestination(R.id.loginFragment);
    }

    public void logout() {
        Log.d("MAIN", "logout()");
        new TokenManager(this).clearAuth();
        goToLoginAndClearBackStack();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
