package com.rc.gcmhelper.storage;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Shared preferences for Bridj app
 * Singleton class
 * Created by akshay on 19/08/14.
 */
public final class StorageHelper {
    private static final String TAG = "###Preferences###";
    private static final String PREF_NAME = "location_data";
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private SharedPreferences.Editor mEditor;


    private static final String PROPERTY_SENDER_ID = "sender_id";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "app_version";

    private static StorageHelper mInstance;

    /**
     * Private constructor
     * @param context
     */
    private StorageHelper(final Context context){
         mSharedPreferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mContext = context;
    }

    /**
     * Factory method to get hold of preference manager
     *
     * @param context context in which app is
     * @return @PreferenceManager
     */
    public static StorageHelper get(final Context context) {
        if (mInstance == null) {
            mInstance = new StorageHelper(context);

        }
        return mInstance;
    }

    public String getGCMRegistrationId(){
        return mSharedPreferences.getString(PROPERTY_REG_ID, "");
    }

    public void storeSenderId(final String senderId){
        saveString(PROPERTY_SENDER_ID,senderId);
    }

    public String getSenderId(){
        return mSharedPreferences.getString(PROPERTY_SENDER_ID,null);
    }

    public void saveGCMRegistrationId(final String regId){
        saveString(PROPERTY_REG_ID, regId);
    }

    private void saveString(final String key, final String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    private void saveBoolean(final String key, final boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    private void saveLong(final String key, final long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    private void saveInt(final String key, final int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

}
