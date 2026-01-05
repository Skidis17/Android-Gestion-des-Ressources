package ma.ensate.myapplication.worker;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class SyncManager {
    public static void enqueueSync(Context ctx) {
        OneTimeWorkRequest req = new OneTimeWorkRequest.Builder(SyncWorker.class).build();
        WorkManager.getInstance(ctx).enqueueUniqueWork("sync_worker", ExistingWorkPolicy.KEEP, req);
    }
}
