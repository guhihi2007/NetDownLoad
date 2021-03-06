package org.network;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2017/5/21.
 */

public class DownLoadTask implements Runnable {

    private final DownLoadEntry entry;
    private volatile boolean isStop;
    private volatile boolean isDelete;
    private Handler handler;

    public DownLoadTask(DownLoadEntry entry, Handler handler) {
        this.entry = entry;
        this.handler = handler;
    }

    public void start() {
        /**
         * 启动ConnectThread，获取是否支持Rang，可以取消
         *
         * 支持用多线程
         * 不支持用单线程
         */
        entry.setStatus(DownLoadEntry.DownloadStatus.downloading);
//        Watched.getInstance().postState(entry);
        setMsg(entry);
        entry.totalLength = 1024 * 50;
        for (int i = entry.getCurrentLength(); i < entry.getTotalLength(); ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isStop || isDelete) {
                entry.status = isStop ? DownLoadEntry.DownloadStatus.paused : DownLoadEntry.DownloadStatus.downloadiedl;
//                Watched.getInstance().postState(entry);
                setMsg(entry);
                return;
            }
            i += 1024;
            int current = entry.currentLength += 1024;
            entry.setCurrentLength(current);
            setMsg(entry);
//            Watched.getInstance().postState(entry);
        }
        entry.setStatus(DownLoadEntry.DownloadStatus.downloaded);
//        Watched.getInstance().postState(entry);
        setMsg(entry);
    }

    public void setMsg(DownLoadEntry entry) {
        Message message = handler.obtainMessage();
        message.obj = entry;
        handler.sendMessage(message);
    }

    public void pause() {
        isStop = true;
    }

    public void delete() {
        isDelete = true;
    }

    @Override
    public void run() {
        start();
    }
}
