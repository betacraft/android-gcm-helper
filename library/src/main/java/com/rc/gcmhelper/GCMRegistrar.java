package com.rc.gcmhelper;

import android.app.Activity;
import android.content.Context;
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


    private Context mContext;
    private GoogleCloudMessaging mGCM;
    private String mRegId;

    private GCMRegistrarListener mListener;


    public interface GCMRegistrarListener {
        void registrationDone(final String regId);

        void registering();

        void errorWhileRegistering(final Throwable exception);
    }

    private GCMRegistrar(final Context context, final String senderId,
                         final GCMRegistrarListener listener) {

        mContext = context;
        mSenderId = senderId;
        mGCM = GoogleCloudMessaging.getInstance(context);
        mRegId = StorageHelper.get(context.getApplicationContext()).getGCMRegistrationId();
        mListener = listener;
    }

    public static void RegisterIfNotRegistered(final Context context,
                                               final String senderId,
                                               final GCMRegistrarListener listener) {
        final String regId = StorageHelper.get(context.getApplicationContext()).getGCMRegistrationId();
        if (!regId.isEmpty()) {
            listener.registrationDone(regId);
            return;
        }
        final GCMRegistrar gcmRegistrar = new GCMRegistrar(context, senderId, listener);
        if (gcmRegistrar.checkPlayServices()) {
            gcmRegistrar.register();
        }
    }

    private void register() {
        mListener.registering();
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.d(TAG, "Registering with Google Servers");
                String msg = "";
                try {
                    if (mGCM == null) {
                        mGCM = GoogleCloudMessaging.getInstance(mContext.getApplicationContext());
                    }
                    mRegId = mGCM.register(mSenderId);
                    Log.d(TAG, "Registration is successful " + mRegId);
                    msg = "Device registered, registration ID=" + mRegId;
                    StorageHelper.get(mContext.getApplicationContext()).saveGCMRegistrationId(mRegId);
                    mListener.registrationDone(mRegId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    mListener.errorWhileRegistering(ex);
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private boolean checkPlayServices() {
        Activity activity;
        try {
            activity = (Activity) mContext;
        } catch (Exception e) {
            return true;
        }
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
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
