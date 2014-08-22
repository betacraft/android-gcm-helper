package com.rc.gcmhelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rc.gcmhelper.storage.StorageHelper;

import java.io.IOException;

/**
 * Helper for mGCM registration
 * Created by akshay on 19/08/14.
 */
public final class GCMRegistrar {

    private static final String TAG = "###GCMRegistrar###";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String mSenderId;


    private Activity mActivity;
    private GoogleCloudMessaging mGCM;
    private String mRegId;

    private GCMRegistrarListener mListener;


    public interface GCMRegistrarListener{
        void registrationDone(final String regId);
        void registering();
        void errorWhileRegistering(final Throwable exception);
    }

    private GCMRegistrar(final Activity activity, final String senderId,
                         final GCMRegistrarListener listener){

        mActivity = activity;
        mSenderId = senderId;
        mGCM = GoogleCloudMessaging.getInstance(activity);
        mRegId=StorageHelper.get(activity.getApplicationContext()).getGCMRegistrationId();
        mListener = listener;
    }

    public static void RegisterIfNotRegistered(final Activity activity,
                                               final String senderId,
                                               final GCMRegistrarListener listener){
        final String regId = StorageHelper.get(activity.getApplicationContext()).getGCMRegistrationId();
        if(!regId.isEmpty()){
            listener.registrationDone(regId);
            return;
        }
        final GCMRegistrar gcmRegistrar = new GCMRegistrar(activity,senderId,listener);
        if(gcmRegistrar.checkPlayServices()){
            gcmRegistrar.register();
        }
    }

    private void register(){
        mListener.registering();
        new AsyncTask<Object,Object,Object>(){
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.d(TAG,"Registering with Google Servers");
                String msg = "";
                try {
                    if (mGCM == null) {
                        mGCM = GoogleCloudMessaging.getInstance(mActivity.getApplicationContext());
                    }
                    mRegId = mGCM.register(mSenderId);
                    Log.d(TAG, "Registration is successful " + mRegId);
                    msg = "Device registered, registration ID=" + mRegId;
                    StorageHelper.get(mActivity.getApplicationContext()).saveGCMRegistrationId(mRegId);
                    mListener.registrationDone(mRegId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    mListener.errorWhileRegistering(ex);
                }
                return msg;
            }
        }.execute(null,null,null);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                mListener.errorWhileRegistering(new IllegalStateException("This device is not supported for GCM"));
            }
            return false;
        }
        return true;
    }

}
