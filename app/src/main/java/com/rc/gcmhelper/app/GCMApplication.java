package com.rc.gcmhelper.app;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.rc.gcmhelper.GCMListener;
import com.rc.gcmhelper.GCMRegistrar;

/**
 * Created by akshay on 22/08/14.
 */
public class GCMApplication extends Application{
    private static final String TAG = "###GCMApplication###";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
