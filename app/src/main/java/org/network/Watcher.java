package org.network;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/5/20.
 */

public abstract class Watcher implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof DownLoadEntry) {
            DownLoadEntry entry=(DownLoadEntry)arg;
            dataUpdate(entry);
        }
    }

    public abstract void dataUpdate(DownLoadEntry entry);
}
