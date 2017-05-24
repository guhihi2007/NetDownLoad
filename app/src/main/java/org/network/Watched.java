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
        map.put(entry.id, entry);//保存状态
        //TODO 保存状态到数据库
        setChanged();
        notifyObservers(entry);
    }

    public ArrayList<DownLoadEntry> getEntryList() {//一键恢复所有暂停的任务
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

    public DownLoadEntry queryDownloadEntries(int id) {
        return map.get(id);
    }
/*第一次启动时服务从数据库拿出数据给Map初始化，Map初始化了，activity内的下载状态才正常
* public void addMap(int id ,DownloadEntry entry){
*  map.put(id,entry)
* }
* */
}
