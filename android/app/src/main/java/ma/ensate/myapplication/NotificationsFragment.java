package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import ma.ensate.myapplication.adapter.NotificationAdapter;
import ma.ensate.myapplication.viewmodel.NotificationViewModel;

public class NotificationsFragment extends Fragment {
    
    public NotificationsFragment() {
        super(R.layout.fragment_notifications);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvNotifications = view.findViewById(R.id.rvNotifications);
        TextView tvUnreadCount = view.findViewById(R.id.tvUnreadCount);
        MaterialButton btnMarkAllRead = view.findViewById(R.id.btnMarkAllRead);
        ProgressBar loading = view.findViewById(R.id.loadingNotifications);
        View emptyState = view.findViewById(R.id.emptyState);

        NotificationAdapter adapter = new NotificationAdapter();
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifications.setAdapter(adapter);

        NotificationViewModel vm = new ViewModelProvider(this).get(NotificationViewModel.class);

        // Observe notifications
        vm.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications == null || notifications.isEmpty()) {
                rvNotifications.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                rvNotifications.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                adapter.setNotifications(notifications);
            }
        });

        // Observe unread count
        vm.getUnreadCount().observe(getViewLifecycleOwner(), count -> {
            long unread = count != null ? count : 0;
            tvUnreadCount.setText(unread + " non lue" + (unread > 1 ? "s" : ""));
        });

        // Observe loading state
        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                loading.setVisibility(View.VISIBLE);
            } else {
                loading.setVisibility(View.GONE);
            }
        });

        // Click handlers
        adapter.setOnNotificationClickListener(notification -> {
            if (notification.estLu != null && !notification.estLu) {
                vm.markAsRead(notification.id);
            }
        });

        btnMarkAllRead.setOnClickListener(v -> vm.markAllAsRead());

        // Initial load
        vm.loadNotifications();
    }
}
