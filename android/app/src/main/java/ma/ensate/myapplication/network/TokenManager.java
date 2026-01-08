package ma.ensate.myapplication.network;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF = "auth_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences sp;

    public TokenManager(Context context) {
        sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void saveAuth(String token, String role) {
        sp.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public String getToken() { return sp.getString(KEY_TOKEN, null); }
    public String getRole() { return sp.getString(KEY_ROLE, null); }
    public void clear() { sp.edit().clear().apply(); }
}
