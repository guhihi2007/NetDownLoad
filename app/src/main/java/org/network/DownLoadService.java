package org.network;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by Administrator on 2017/5/20.
 */

public class DownLoadService extends Service {

    private HashMap<Integer, DownLoadTask> mDownLoadTask = new HashMap<>();
    private ExecutorService executors;
    private LinkedBlockingQueue waitQeue = new LinkedBlockingQueue();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownLoadEntry entry = (DownLoadEntry) msg.obj;
            Watched.getInstance().postState(entry);
            switch (entry.status) {
                case downloaded:
                case paused:
                case canceled:
                    mDownLoadTask.remove(entry);
                    startNext();
            }
        }
    };

    private void startNext() {
        DownLoadEntry e = (DownLoadEntry) waitQeue.poll();
        if (e != null)
            if (e.status == DownLoadEntry.DownloadStatus.waiting) {
                start(e);
            }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executors = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownLoadEntry entry = (DownLoadEntry) intent.getSerializableExtra(Constants.DownLoadEntry);
        int action = intent.getIntExtra(Constants.DownLoadAction, -1);
        doAction(action, entry);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(int action, DownLoadEntry entry) {
        switch (action) {
            case Constants.DownLoadStart:
                addTask(entry);
                break;
            case Constants.DownLoadStop:
                stop(entry);
                break;
            case Constants.DownLoadDelete:
                delete(entry);
                break;
            case Constants.DownLoadResume:
                resume(entry);
                break;
        }
    }

    private void addTask(DownLoadEntry entry) {
        if (mDownLoadTask.size() > Constants.MAX_DOWNLOAD_TASK) {
            waitQeue.offer(entry);
            entry.status = DownLoadEntry.DownloadStatus.waiting;
            Watched.getInstance().postState(entry);
        } else {
            start(entry);
        }
    }

    private void start(DownLoadEntry entry) {
        DownLoadTask task = new DownLoadTask(entry, handler);
//        task.start();
        executors.execute(task);
        mDownLoadTask.put(entry.id, task);
    }

    private void stop(DownLoadEntry entry) {
        DownLoadTask task = mDownLoadTask.remove(entry.id);
        if (task != null) {
            task.stop();
        } else {
            waitQeue.remove(entry);
            entry.status = DownLoadEntry.DownloadStatus.paused;
            Watched.getInstance().postState(entry);
        }
    }

    private void resume(DownLoadEntry entry) {
        addTask(entry);
    }

    private void delete(DownLoadEntry entry) {
        DownLoadTask task = mDownLoadTask.remove(entry.id);
        if (task != null) {
            task.delete();
        } else {
            waitQeue.remove(entry);
            entry.status = DownLoadEntry.DownloadStatus.downloadiedl;
            Watched.getInstance().postState(entry);
        }
    }


}
