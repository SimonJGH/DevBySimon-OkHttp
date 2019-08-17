package com.simon.devbysimon_okhttp;

import android.util.Log;

public class L {

    private static boolean debug = true;
    private static String TAG = "Simon";

    public static void e(String msg) {
        if (debug) {
            Log.i(TAG, msg);
        }
    }
}
