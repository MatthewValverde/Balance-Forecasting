package chitoiu.com.balance.forecasting.utils;

import android.content.Context;
import android.content.SharedPreferences;

import chitoiu.com.balance.forecasting.R;

/**
 * Creates a class for accessing category filters and saving them to shared preferences.
 */
public class SavedPreferences {

    private Context mContext;
    private SharedPreferences mSharedPrefs;

    public static String SHARED_PREFERENCES_LOGIN = "SHARED_PREFERENCES_LOGIN";

    public SavedPreferences(Context context, String sharedPrefsString) {
        mContext = context;
        mSharedPrefs = mContext.getSharedPreferences(sharedPrefsString, Context.MODE_PRIVATE);
    }

    public void setLoginSharedPreferences(String email, String password) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(mContext.getString(R.string.saved_prefs_login_email), email);
        editor.putString(mContext.getString(R.string.saved_prefs_login_password), password);
        editor.apply();
    }

    public String getLoginString() {
        return mSharedPrefs.getString(mContext.getString(R.string.saved_prefs_login_email), null);
    }

    public String getPasswordString() {
        return mSharedPrefs.getString(mContext.getString(R.string.saved_prefs_login_password), null);
    }
}