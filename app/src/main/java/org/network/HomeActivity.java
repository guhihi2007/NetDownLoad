package org.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button pause,delete;
    private DownLoadManager manager;
    private DownLoadEntry entry;
    private Watcher watcher = new Watcher() {
        @Override
        public void dataUpdate(DownLoadEntry data) {
            entry = data;//保存entry的状态，恢复下载时获取已下载的数据
            if (entry.status== DownLoadEntry.DownloadStatus.downloadiedl)
                entry=null;
            Gpp.v(data.getUrl() + "  is " + data.getStatus() + "  " + data.currentLength + "/" + data.totalLength);
//            if (entry.getStatus() == DownLoadEntry.DownloadStatus.downloading) {
//                start.setText("正在下载中...");
//                pause.setText("暂停");
//            }else if (entry.getStatus() == DownLoadEntry.DownloadStatus.paused){
//                pause.setText("已暂停");
//                start.setText("继续");
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        manager = DownLoadManager.getInstance(this);
        start = (Button) findViewById(R.id.download);
        pause = (Button) findViewById(R.id.pause);
        delete=(Button) findViewById(R.id.delete);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.addObserver(watcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeObserver(watcher);
    }

    @Override
    public void onClick(View v) {
//        if (entry == null) {
//            entry = new DownLoadEntry();
//            entry.setUrl("http://baidu.com");
//            entry.status = DownLoadEntry.DownloadStatus.downloadiedl;
//        }
        switch (v.getId()) {
            case R.id.download:
                if (entry.status == DownLoadEntry.DownloadStatus.downloadiedl)
                    manager.start(entry);
                break;
            case R.id.pause:
                if (entry.status == DownLoadEntry.DownloadStatus.downloading) {
                    manager.pause(entry);
                } else if (entry.status == DownLoadEntry.DownloadStatus.paused) {
                    manager.resume(entry);
                }
                break;
            case R.id.delete:
                manager.delete(entry);
        }
    }
}
