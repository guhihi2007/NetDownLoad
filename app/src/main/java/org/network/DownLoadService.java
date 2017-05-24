package org.network;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.ArrayList;
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
    private LinkedBlockingQueue<DownLoadEntry> waitQeue = new LinkedBlockingQueue();
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
        DownLoadEntry e = waitQeue.poll();
        if (e != null)
            if (e.status == DownLoadEntry.DownloadStatus.waiting) {
                addTask(e);
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
        //服务第一次启动时，watched里面的map是空的，要在服务启动的时候给他数据，activity初始化就能获取到状态
        //TODO 从数据库中拿出所有的数据
        /*
           ArrayList<DownloadEntry> list = DataBaseGetDownLoadEntry();
           if(list != null)
               for ( DownloadEntry entry:list ){
                  if(entry.status==downloading||waiting){ //因为程序被强杀后所有状态都保存到数据库，再次拿出来的时候要判断，不然activity显示不一致
                  entry.status=paused
                  addTask（entry）
                   // to do 或者可以在这里恢复，继续下载
                  }
                 Watched.getInstance.addMap(entry.id,entry);
                }
            * */
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
            case Constants.DownLoadPause:
                pause(entry);
                break;
            case Constants.DownLoadDelete:
                delete(entry);
                break;
            case Constants.DownLoadResume:
                resume(entry);
                break;
            case Constants.PauseAll:
                pauseAll();
                break;
            case Constants.RecoveruAll:
                recoveryAll();
                break;
        }
    }

    private void recoveryAll() {
        ArrayList<DownLoadEntry> list = Watched.getInstance().getEntryList();
        if (list != null)
            for (DownLoadEntry e : list) {
                addTask(e);
            }
    }

    private void pauseAll() {
        while (waitQeue.iterator().hasNext()) {
            DownLoadEntry entry = waitQeue.poll();
            entry.status = DownLoadEntry.DownloadStatus.paused;
            Watched.getInstance().postState(entry);
        }
        for (HashMap.Entry<Integer, DownLoadTask> entry : mDownLoadTask.entrySet()) {
            entry.getValue().pause();
        }
        mDownLoadTask.clear();
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

    private void pause(DownLoadEntry entry) {
        DownLoadTask task = mDownLoadTask.remove(entry.id);
        if (task != null) {
            task.pause();
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
