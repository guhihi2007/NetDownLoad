package org.network;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DownLoadManager manager;
    private ArrayList<DownLoadEntry> arrayList = new ArrayList<>();
    private Adapter adapter;
    private TextView pauseall;
    private Watcher watcher = new Watcher() {
        @Override
        public void dataUpdate(DownLoadEntry entry) {
            int index = arrayList.indexOf(entry);
            if (index != -1) {
                arrayList.remove(index);
                arrayList.add(index, entry);
                adapter.notifyDataSetChanged();
            }
        }
    };
    private TextView recovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = DownLoadManager.getInstance(this);//获取DownLoadManager时，会启动服务，但是服务启动的时间会比activity初始化会慢，可以在启动页时就初始化
        initData();//每次onCreate拿到的不是最新的数据，所以在这里拿最新的数据状态
        findView();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            String url = "test" + i;
            DownLoadEntry entry = new DownLoadEntry();
            entry.setUrl(url);
            entry.id = i;
            arrayList.add(entry);
        }
        DownLoadEntry oldEntry = null;
        DownLoadEntry newEntry = null;
        for (int i = 0; i < arrayList.size(); i++) {
            oldEntry = arrayList.get(i);
            newEntry = manager.queryDownloadEntry(oldEntry.id);//不能直接操作被观察者，所以用manager来操作
            if (newEntry != null) {
                arrayList.remove(i);
                arrayList.add(i, newEntry);
            }
        }
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

    public void findView() {


        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        pauseall = (TextView) findViewById(R.id.pauseAll);
        recovery = (TextView) findViewById(R.id.tv);
        pauseall.setText("PauseAll");
        recovery.setText("RecoveryAll");
        pauseall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.pauseAll();
            }
        });
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.recoveryAll();
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private final Context context;
        private ArrayList<DownLoadEntry> map;

        public Adapter(Context context, ArrayList map) {
            this.context = context;
            this.map = map;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = null;
            View view = LayoutInflater.from(context).inflate(R.layout.viewholder_layout, parent, false);
            holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (holder instanceof ViewHolder) {
                holder.textView.setText(map.get(position).getUrl());
                holder.textView2.setText(map.get(position).currentLength + " is " + map.get(position).status);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (map.get(position).status == DownLoadEntry.DownloadStatus.downloadiedl) {
                            manager.start(map.get(position));
                        } else if (map.get(position).status == DownLoadEntry.DownloadStatus.downloading || map.get(position).status == DownLoadEntry.DownloadStatus.waiting) {
                            manager.pause(map.get(position));
                        } else if (map.get(position).status == DownLoadEntry.DownloadStatus.paused) {
                            manager.resume(map.get(position));
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return map.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, textView2;
        private Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.vh_tv);
            textView2 = (TextView) itemView.findViewById(R.id.vh_tv2);
            button = (Button) itemView.findViewById(R.id.vh_download);
        }
    }
}
