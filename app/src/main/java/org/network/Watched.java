package org.network;

import java.util.Observable;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Watched extends Observable {
    private static Watched watched;

    private Watched() {
    }

    public static synchronized Watched getInstance() {
        if (watched == null) {
            watched = new Watched();
        }
        return watched;
    }

    public void postState(DownLoadEntry entry) {
        setChanged();
        notifyObservers(entry);
    }
}
