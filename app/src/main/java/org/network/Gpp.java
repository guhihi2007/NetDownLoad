package org.network;

import android.util.Log;

/**
 * Created by Administrator on 2017/5/10.
 */

public class Gpp {
    public static final String TAG = "gpp";
    private static boolean isShow=true;

    public static void v(String msg) {
        if (isShow) {
            Log.v(TAG, msg);
        }
    }
}
