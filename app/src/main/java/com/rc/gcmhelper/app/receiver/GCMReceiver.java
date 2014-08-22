package com.rc.gcmhelper.app.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;



/**
 * GCM message reciever
 * Created by akshay on 19/08/14.
 */
public final class GCMReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "###GCMReciever###";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG,"Got :" +intent.getExtras().getString("from"));

    }
}
