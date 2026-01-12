package ma.ensate.myapplication.repository;

import ma.ensate.myapplication.model.Notification;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import retrofit2.Call;

import java.util.List;
import java.util.Map;

public class NotificationRepository {
    private final ApiService api;

    public NotificationRepository() {
        this.api = RetrofitClient.api();
    }

    public Call<List<Notification>> getNotifications() {
        return api.getNotifications();
    }

    public Call<Map<String, Long>> getUnreadCount() {
        return api.getUnreadCount();
    }

    public Call<Notification> markAsRead(Long id) {
        return api.markNotificationAsRead(id);
    }

    public Call<Void> markAllAsRead() {
        return api.markAllNotificationsAsRead();
    }
}
