package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensate.myapplication.model.Notification;
import ma.ensate.myapplication.repository.NotificationRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

public class NotificationViewModel extends ViewModel {
    private final NotificationRepository repository = new NotificationRepository();
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    private final MutableLiveData<Long> unreadCount = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public LiveData<Long> getUnreadCount() {
        return unreadCount;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadNotifications() {
        loading.postValue(true);
        repository.getNotifications().enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    notifications.postValue(response.body());
                    updateUnreadCount(response.body());
                } else {
                    notifications.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                loading.postValue(false);
                notifications.postValue(null);
            }
        });
    }

    public void loadUnreadCount() {
        repository.getUnreadCount().enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    unreadCount.postValue(response.body().get("count"));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                // Silent fail
            }
        });
    }

    public void markAsRead(Long notificationId) {
        repository.markAsRead(notificationId).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    loadNotifications(); // Refresh list
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                // Silent fail
            }
        });
    }

    public void markAllAsRead() {
        repository.markAllAsRead().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadNotifications(); // Refresh list
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Silent fail
            }
        });
    }

    private void updateUnreadCount(List<Notification> notificationList) {
        if (notificationList == null) {
            unreadCount.postValue(0L);
            return;
        }
        long count = notificationList.stream()
                .filter(n -> n.estLu != null && !n.estLu)
                .count();
        unreadCount.postValue(count);
    }
}
