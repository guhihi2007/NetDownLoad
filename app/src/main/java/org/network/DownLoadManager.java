package org.network;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/5/20.
 */

public class DownLoadManager {

    private static DownLoadManager manager;
    private Context context;

    private DownLoadManager(Context context) {
        this.context = context;
    }

    public static synchronized DownLoadManager getInstance(Context context) {
        if (manager == null) {
            manager = new DownLoadManager(context);
        }
        return manager;
    }

    public void start(DownLoadEntry entry) {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadService.class);
        intent.putExtra(Constants.DownLoadEntry, entry);
        intent.putExtra(Constants.DownLoadAction, Constants.DownLoadStart);
        context.startService(intent);
    }

    public void pause(DownLoadEntry entry) {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadService.class);
        intent.putExtra(Constants.DownLoadEntry, entry);
        intent.putExtra(Constants.DownLoadAction, Constants.DownLoadPause);
        context.startService(intent);
    }

    public void resume(DownLoadEntry entry) {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadService.class);
        intent.putExtra(Constants.DownLoadEntry, entry);
        intent.putExtra(Constants.DownLoadAction, Constants.DownLoadResume);
        context.startService(intent);
    }

    public void delete(DownLoadEntry entry) {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadService.class);
        intent.putExtra(Constants.DownLoadEntry, entry);
        intent.putExtra(Constants.DownLoadAction, Constants.DownLoadDelete);
        context.startService(intent);
    }

    public void addObserver(Watcher watcher) {
        Watched.getInstance().addObserver(watcher);
    }

    public void removeObserver(Watcher watcher) {
        Watched.getInstance().deleteObserver(watcher);
    }

    public void pauseAll() {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadService.class);
        intent.putExtra(Constants.DownLoadAction, Constants.PauseAll);
        context.startService(intent);
    }

    public void recoveryAll() {
        Intent intent = new Intent();
        intent.setClass(context, DownLoadService.class);
        intent.putExtra(Constants.DownLoadAction, Constants.RecoveruAll);
        context.startService(intent);
    }
}
