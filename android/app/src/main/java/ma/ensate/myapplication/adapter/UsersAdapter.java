package ma.ensate.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.UserItem;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.VH> {

    public interface OnUserClickListener {
        void onUserClick(UserItem user);
    }

    private final List<UserItem> data = new ArrayList<>();
    private OnUserClickListener listener;

    public UsersAdapter() {}

    public void setListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<UserItem> users) {
        data.clear();
        if (users != null) data.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        UserItem u = data.get(position);

        String username = safe(u.username);
        String email = safe(u.email);
        String role = (u.role == null) ? "" : u.role.toString();

        h.tvUsername.setText(username.isEmpty() ? "(sans username)" : username);
        h.tvEmail.setText(email.isEmpty() ? "(sans email)" : email);
        h.tvRole.setText(role.toUpperCase(Locale.ROOT));

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onUserClick(u);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail, tvRole;

        VH(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRole = itemView.findViewById(R.id.tvRole);
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
