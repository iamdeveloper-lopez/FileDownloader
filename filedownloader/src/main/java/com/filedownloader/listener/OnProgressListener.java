package com.filedownloader.listener;

public interface OnProgressListener {

    void onProgress(long bytesWritten, long totalSize);

}
