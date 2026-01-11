package ma.ensate.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ma.ensate.myapplication.adapter.UsersAdapter;
import ma.ensate.myapplication.model.UserItem;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsersFragment extends Fragment {

    private TextView tvTotalUsers, tvAdminCount, tvRhCount, tvRecruteurCount, tvDirecteurCount;

    private RecyclerView rvUsers;
    private UsersAdapter adapter;
    private EditText etSearchUser;
    private View fabAddUser;

    private ApiService api;
    private List<UserItem> allUsers = new ArrayList<>();

    public AdminUsersFragment() {
        super(R.layout.fragment_admin_users);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api = RetrofitClient.api(); // ✅ plus de requireContext()

        // ✅ init TextViews (sinon NullPointerException)
        tvTotalUsers = view.findViewById(R.id.tvTotalUsers);
        tvAdminCount = view.findViewById(R.id.tvAdminCount);
        tvRhCount = view.findViewById(R.id.tvRhCount);
        tvRecruteurCount = view.findViewById(R.id.tvRecruteurCount);
        tvDirecteurCount = view.findViewById(R.id.tvDirecteurCount);

        rvUsers = view.findViewById(R.id.rvUsers);
        etSearchUser = view.findViewById(R.id.etSearchUser);
        fabAddUser = view.findViewById(R.id.fabAddUser);

        adapter = new UsersAdapter();
        rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvUsers.setAdapter(adapter);

        loadUsers();

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }
        });

        fabAddUser.setOnClickListener(v -> openAddBottomSheet());
    }

    private String authHeader() {
        String token = new TokenManager(requireContext()).getToken();
        if (token == null || token.trim().isEmpty()) return null;
        return "Bearer " + token;
    }

    private void openAddBottomSheet() {
        AddUserBottomSheet bs = new AddUserBottomSheet();
        bs.setListener(this::loadUsers);
        bs.show(getParentFragmentManager(), "AddUserBottomSheet");
    }

    private void loadUsers() {
        String auth = authHeader();
        if (auth == null) {
            Toast.makeText(requireContext(), "Connecte-toi d'abord", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ ApiService doit être: getUsers(@Header("Authorization") String auth)
        api.getUsers(auth).enqueue(new Callback<List<UserItem>>() {
            @Override
            public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(),
                            "Erreur chargement users (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                allUsers = response.body();

                tvTotalUsers.setText(String.valueOf(allUsers.size()));

                int admin = 0, rh = 0, recruteur = 0, directeur = 0;
                for (UserItem u : allUsers) {
                    String r = (u.role == null) ? "" : u.role.trim();
                    if ("admin".equalsIgnoreCase(r)) admin++;
                    else if ("RH".equalsIgnoreCase(r)) rh++;
                    else if ("recruteur".equalsIgnoreCase(r)) recruteur++;
                    else if ("directeur".equalsIgnoreCase(r)) directeur++;
                }

                tvAdminCount.setText(String.valueOf(admin));
                tvRhCount.setText(String.valueOf(rh));
                tvRecruteurCount.setText(String.valueOf(recruteur));
                tvDirecteurCount.setText(String.valueOf(directeur));

                adapter.setData(allUsers);
            }

            @Override
            public void onFailure(Call<List<UserItem>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur réseau users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers(String q) {
        String query = (q == null) ? "" : q.trim().toLowerCase(Locale.ROOT);

        if (query.isEmpty()) {
            adapter.setData(allUsers);
            return;
        }

        List<UserItem> filtered = new ArrayList<>();
        for (UserItem u : allUsers) {
            String email = (u.email != null) ? u.email.toLowerCase(Locale.ROOT) : "";
            String username = (u.username != null) ? u.username.toLowerCase(Locale.ROOT) : "";
            if (email.contains(query) || username.contains(query)) filtered.add(u);
        }
        adapter.setData(filtered);
    }
}
