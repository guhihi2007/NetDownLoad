package org.network;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/20.
 * 下载所包含的数据,url,length等
 */

public class DownLoadEntry implements Serializable {

    public String url;
    public DownloadStatus status =DownloadStatus.downloadiedl;
    public int currentLength;
    public int totalLength;
    public int id;


    public DownLoadEntry() {
//        this.url = url;
//        int indexOf = url.lastIndexOf(".");
//        this.id = url.substring(indexOf, url.length());
//        this.status=DownloadStatus.downloadiedl;
    }

    enum DownloadStatus {
        downloading, downloaded, paused, downloadiedl, waiting, canceled
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode()==this.hashCode();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
