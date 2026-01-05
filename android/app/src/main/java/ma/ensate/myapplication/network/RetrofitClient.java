package ma.ensate.myapplication.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String DEFAULT_BASE_URL = "http://192.168.11.106:8080/"; // use 10.0.2.2 for emulator to reach host
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        return getClient(DEFAULT_BASE_URL);
    }

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService api() {
        return getClient().create(ApiService.class);
    }
}