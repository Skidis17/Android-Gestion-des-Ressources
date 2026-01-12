package ma.ensate.myapplication.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Notification;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notifications = new ArrayList<>();
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications != null ? notifications : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification, listener);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final View indicatorView;
        private final TextView tvTitre;
        private final TextView tvMessage;
        private final TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardNotification);
            indicatorView = itemView.findViewById(R.id.unreadIndicator);
            tvTitre = itemView.findViewById(R.id.tvNotificationTitre);
            tvMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvDate = itemView.findViewById(R.id.tvNotificationDate);
        }

        public void bind(Notification notification, OnNotificationClickListener listener) {
            tvTitre.setText(notification.titre);
            tvMessage.setText(notification.message);
            tvDate.setText(formatDate(notification.createdAt));

            // Show/hide unread indicator
            if (notification.estLu != null && !notification.estLu) {
                indicatorView.setVisibility(View.VISIBLE);
                cardView.setCardBackgroundColor(Color.parseColor("#F0F9FF")); // Light blue for unread
            } else {
                indicatorView.setVisibility(View.GONE);
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            // Set color based on type
            int color = getColorForType(notification.type);
            indicatorView.setBackgroundColor(color);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNotificationClick(notification);
                }
            });
        }

        private String formatDate(String dateStr) {
            if (dateStr == null) return "";
            try {
                // Parse ISO 8601 format: 2026-01-12T17:30:00
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormatter);
                
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.FRENCH);
                return dateTime.format(outputFormatter);
            } catch (Exception e) {
                return dateStr;
            }
        }

        private int getColorForType(String type) {
            if (type == null) return Color.GRAY;
            switch (type.toUpperCase()) {
                case "SUCCESS":
                    return Color.parseColor("#10B981"); // Green
                case "WARNING":
                    return Color.parseColor("#F59E0B"); // Orange
                case "ERROR":
                    return Color.parseColor("#EF4444"); // Red
                case "INFO":
                default:
                    return Color.parseColor("#3B82F6"); // Blue
            }
        }
    }
}
