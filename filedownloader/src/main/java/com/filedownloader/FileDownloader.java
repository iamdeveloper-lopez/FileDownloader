package com.filedownloader;

import android.content.Context;
import android.util.Log;

import com.filedownloader.listener.OnFailedListener;
import com.filedownloader.listener.OnFinishListener;
import com.filedownloader.listener.OnProgressListener;
import com.filedownloader.listener.OnStartListener;
import com.filedownloader.listener.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.commons.io.FileUtils;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class FileDownloader {

    private static final String TAG = FileDownloader.class.getSimpleName();

    private Context context;
    private AsyncHttpClient client;
    private String url;
    private File outputFile;

    private OnStartListener onStartListener;
    private OnProgressListener onProgressListener;
    private OnSuccessListener onSuccessListener;
    private OnFailedListener onFailedListener;
    private OnFinishListener onFinishListener;

    FileDownloader(Builder builder) {
        this.context = builder.context;
        this.client = builder.client;
        this.url = builder.url;
        this.outputFile = builder.outputFile;
        this.onStartListener = builder.onStartListener;
        this.onProgressListener = builder.onProgressListener;
        this.onSuccessListener = builder.onSuccessListener;
        this.onFailedListener = builder.onFailedListener;
        this.onFinishListener = builder.onFinishListener;

        this.client.get(url, new FileAsyncHttpResponseHandler(context) {

            @Override
            public void onStart() {
                if (onStartListener != null) {
                    onStartListener.onStart();
                } else {
                    Log.d(TAG, "OnStartListener not initialize");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                if (onFailedListener != null) {
                    onFailedListener.onFailed(throwable);
                } else {
                    Log.d(TAG, "OnFailedListener not initialize");
                    Log.e(TAG, "onFailure: ", throwable);
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                if (onProgressListener != null) {
                    onProgressListener.onProgress(bytesWritten, totalSize);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File fileFromNetwork) {
                if (onSuccessListener != null) {
                    if (outputFile != null) {
                        try {
                            FileUtils.copyFile(fileFromNetwork, outputFile);
                            if (fileFromNetwork.exists()) {
                                FileUtils.deleteQuietly(fileFromNetwork);
                            }
                            onSuccessListener.onSuccess(outputFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (onFailedListener != null)
                                onFailedListener.onFailed(e);
                            else
                                Log.d(TAG, "OnFailedListener not initialize");
                        }
                    } else {
                        onSuccessListener.onSuccess(fileFromNetwork);
                    }
                } else {
                    Log.d(TAG, "OnSuccessListener not initialize");
                }
            }

            @Override
            public void onFinish() {
                if (onFinishListener != null) {
                    onFinishListener.onFinish();
                } else {
                    Log.d(TAG, "OnFinishListener not initialize");
                }
            }
        });
    }

    public static final class Builder {

        Context context;
        AsyncHttpClient client;
        String url;
        File outputFile;

        //Listeners
        OnStartListener onStartListener;
        OnProgressListener onProgressListener;
        OnSuccessListener onSuccessListener;
        OnFailedListener onFailedListener;
        OnFinishListener onFinishListener;

        public Builder(Context context) {
            this.context = context;
            this.client = new AsyncHttpClient();
            this.client.setTimeout(20 * 1000);
            this.client.setConnectTimeout(20 * 1000);
            this.client.setResponseTimeout(20 * 1000);
            this.client.setLoggingEnabled(true);
            this.client.setLoggingLevel(Log.DEBUG);
        }

        public Builder client(AsyncHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder outputFile(File outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public Builder listener(OnStartListener listener) {
            this.onStartListener = listener;
            return this;
        }

        public Builder listener(OnProgressListener listener) {
            this.onProgressListener = listener;
            return this;
        }

        public Builder listener(OnSuccessListener listener) {
            this.onSuccessListener = listener;
            return this;
        }

        public Builder listener(OnFailedListener listener) {
            this.onFailedListener = listener;
            return this;
        }

        public Builder listener(OnFinishListener listener) {
            this.onFinishListener = listener;
            return this;
        }

        public FileDownloader build() {
            return new FileDownloader(this);
        }

    }

}
