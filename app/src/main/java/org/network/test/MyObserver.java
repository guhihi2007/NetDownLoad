package org.network.test;

import android.util.Log;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MyObserver implements IObserver {
    @Override
    public void updata(String s) {
        Log.v("gpp",s);
    }
}
