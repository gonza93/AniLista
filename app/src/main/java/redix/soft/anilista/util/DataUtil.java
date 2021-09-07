package redix.soft.anilista.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import redix.soft.anilista.R;

public class DataUtil {

    public enum DATA { QUERY, USERNAME, SAVED_USER, MODE, AUTHCODE, VERIFIER, TOKEN, REFRESH, EXPIRE, SUPPORT, DONTSHOW }
    private Context context;
    private boolean firstInstance;
    private static DataUtil instance;

    public static DataUtil getInstance(Context context){
        return instance == null? instance = new DataUtil(context) : instance;
    }

    private DataUtil(Context context) {
        this.context = context;
        firstInstance = true;
    }

    private SharedPreferences getSharedPreferences(){
        String fileKey = context.getPackageName() + context.getString(R.string.preference_file_key);
        return context.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
    }

    public void saveInt(String key, int data){
        firstInstance = false;

        getSharedPreferences()
                .edit()
                .putInt(key, data)
                .apply();
    }

    public void saveString(String key, String data){
        firstInstance = false;

        getSharedPreferences()
                .edit()
                .putString(key, data)
                .apply();
    }

    public void saveBoolean(String key, boolean data){
        firstInstance = false;

        getSharedPreferences()
                .edit()
                .putBoolean(key, data)
                .apply();
    }

    public String getLastSearch(){
        return firstInstance? null : getSharedPreferences().getString(DATA.QUERY.toString(), null);
    }

    public String getSavedUsername(){
        return getSharedPreferences().getString(DATA.SAVED_USER.toString(), null);
    }

    public String getString(String key){
        return getSharedPreferences().getString(key, null);
    }
    public int getInt(String key){
        return getSharedPreferences().getInt(key, 0);
    }

    public int getUIMode(){
        return getSharedPreferences().getInt(DATA.MODE.toString(), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public boolean showSupport(){
        boolean dontShow = getSharedPreferences().getBoolean(DATA.DONTSHOW.toString(), false);
        int timesOpened = getSharedPreferences().getInt(DATA.SUPPORT.toString(), 0);
        timesOpened++;

        if (dontShow)
            return false;

        if (timesOpened == 3)
            return true;
        else {
            saveInt(DATA.SUPPORT.toString(), timesOpened);
            return false;
        }
    }
}
