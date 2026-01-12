package ma.ensate.myapplication;

import android.app.Application;

import ma.ensate.myapplication.network.RetrofitClient;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.init(this);
    }
}
