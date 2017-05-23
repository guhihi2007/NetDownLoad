package org.network;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Watched extends Observable {
    private static Watched watched;
    private LinkedHashMap<Integer, DownLoadEntry> map;

    private Watched() {
        map = new LinkedHashMap<>();
    }

    public static synchronized Watched getInstance() {
        if (watched == null) {
            watched = new Watched();
        }
        return watched;
    }

    public void postState(DownLoadEntry entry) {
        map.put(entry.id, entry);
        setChanged();
        notifyObservers(entry);
    }

    public ArrayList<DownLoadEntry> getEntryList() {
        ArrayList<DownLoadEntry> arrayList = null;
        for (Map.Entry<Integer, DownLoadEntry> e : map.entrySet()) {
            if (e.getValue().status == DownLoadEntry.DownloadStatus.paused) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(e.getValue());
            }
        }
        return arrayList;
    }
}
