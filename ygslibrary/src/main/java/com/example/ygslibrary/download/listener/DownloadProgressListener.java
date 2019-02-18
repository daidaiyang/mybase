package com.example.ygslibrary.download.listener;

public interface DownloadProgressListener {

    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
