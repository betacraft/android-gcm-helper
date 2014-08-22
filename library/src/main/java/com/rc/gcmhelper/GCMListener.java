package com.rc.gcmhelper;

/**
 * Created by akshay on 22/08/14.
 */
public abstract class GCMListener{
    abstract void onMessageReceived(final String message);
}
