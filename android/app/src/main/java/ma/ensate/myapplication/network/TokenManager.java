package ma.ensate.myapplication.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private static final String PREF = "auth_pref";

    // ✅ KEYS (TOUJOURS String)
    private static final String KEY_ID = "user_id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences sp;

    public TokenManager(Context context) {
        sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    // ✅ SAVE USER DATA
    public void saveAuth(Long id, String token, String role, String username, String email) {
        sp.edit()
                .putLong(KEY_ID, id != null ? id : -1L)
                .putString(KEY_TOKEN, token)
                .putString(KEY_ROLE, role)
                .putString(KEY_USERNAME, username)
                .putString(KEY_EMAIL, email)
                .apply();

        logUserData();
    }

    // ✅ GETTERS
    public Long getUserId() {
        return sp.getLong(KEY_ID, -1L);
    }

    public String getToken() {
        return sp.getString(KEY_TOKEN, null);
    }

    public String getRole() {
        return sp.getString(KEY_ROLE, null);
    }

    public String getUsername() {
        return sp.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return sp.getString(KEY_EMAIL, "");
    }

    // ✅ DEBUG
    public void logUserData() {
        Log.d("TOKEN", "id=" + getUserId());
        Log.d("TOKEN", "token=" + getToken());
        Log.d("TOKEN", "role=" + getRole());
        Log.d("TOKEN", "username=" + getUsername());
        Log.d("TOKEN", "email=" + getEmail());
    }

    // ✅ CLEAR
    public void clearAuth() {
        sp.edit().clear().apply();
        Log.d("TOKEN", "Auth cleared");
    }
}
