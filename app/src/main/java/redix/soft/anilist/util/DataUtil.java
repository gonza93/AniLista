package redix.soft.anilist.util;

import android.content.Context;
import android.content.SharedPreferences;

import redix.soft.anilist.R;

public class DataUtil {

    public enum DATA { QUERY, USERNAME, SAVED_USER }
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

    public String getLastSearch(){
        return firstInstance? null : getSharedPreferences().getString(DATA.QUERY.toString(), null);
    }

    public String getLastUsername(){
        return firstInstance? null : getSharedPreferences().getString(DATA.QUERY.toString(), null);
    }

    public String getSavedUsername(){
        return getSharedPreferences().getString(DATA.SAVED_USER.toString(), null);
    }
}
