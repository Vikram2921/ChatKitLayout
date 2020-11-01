package com.nobodyknows.chatlayoutview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;


import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.nobodyknows.chatlayoutview.Model.SharedFile;

import java.io.File;
import java.util.ArrayList;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class DownloadHelper {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Context context;
    private DownloadManager downloadManager;
    private Activity activity;
    public DownloadHelper(Context context, Activity activity) {
        this.context =context;
        this.activity = activity;
        downloadManager = DownloadService.getDownloadManager(context);
    }

    public int download(String url,String dirPath,String fileName) {
        int downloadId = 0;
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            String envPath = Environment.getExternalStorageDirectory().getPath();
            if (!new File(envPath +dirPath).exists()) {
                new File(envPath + dirPath).mkdirs();
            }
        }
        return downloadId;
    }

    private Double getSum(ArrayList<Integer> list) {
        Double result = 0.0;
        for(Integer d:list) {
            result += d;
        }
        return result;
    }

    public int downloadAll(ArrayList<SharedFile> urls, String dirPath, CircularProgressButton progressBar) {
        int downloadId = 0;
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            String envPath = Environment.getExternalStorageDirectory().getPath();
            if (!new File(envPath +dirPath).exists()) {
                new File(envPath + dirPath).mkdirs();
            }
            int totalDownloads = urls.size();
            final int[] downloadCompleted = {0};
            String filename = "";
            for(int i=0;i<urls.size();i++) {
                if(!new File(envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension()).exists()) {
                    DownloadInfo downloadInfo  = new DownloadInfo.Builder().setUrl(urls.get(i).getUrl()).setPath(envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension()).build();
                    int finalTotalDownloads = totalDownloads;
                    downloadInfo.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onWaited() {

                        }

                        @Override
                        public void onPaused() {

                        }

                        @Override
                        public void onDownloading(long progress, long size) {
                        }

                        @Override
                        public void onRemoved() {

                        }

                        @Override
                        public void onDownloadSuccess() {
                            downloadCompleted[0]++;
                            float progress = ((float) downloadCompleted[0] / (float) finalTotalDownloads)*100;
                            progressBar.setProgress(progress);
                            if(progress == 100.0) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onDownloadFailed(DownloadException e) {

                        }
                    });
                    downloadManager.download(downloadInfo);
                } else {
                    downloadCompleted[0]++;
                    float progress = ((float) downloadCompleted[0] / (float) totalDownloads)*100;
                    progressBar.setProgress(progress);
                    if(progress == 100.0) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }
        return downloadId;
    }

    public int downloadSingle(SharedFile sharedFile, String dirPath, CircularProgressButton progressBar) {
        int downloadId = 0;
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            String envPath = Environment.getExternalStorageDirectory().getPath();
            if (!new File(envPath +dirPath).exists()) {
                new File(envPath + dirPath).mkdirs();
            }
            if(!new File(envPath+dirPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).exists()) {
                DownloadInfo downloadInfo  = new DownloadInfo.Builder().setUrl(sharedFile.getUrl()).setPath(envPath+dirPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).build();
                downloadInfo.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onWaited() {

                    }

                    @Override
                    public void onPaused() {

                    }

                    @Override
                    public void onDownloading(long progress, long size) {
                        progressBar.setProgress(calculateProgress(progress,size));
                    }

                    @Override
                    public void onRemoved() {

                    }

                    @Override
                    public void onDownloadSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDownloadFailed(DownloadException e) {

                    }
                });
                downloadManager.download(downloadInfo);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
        return downloadId;
    }

    private int calculateProgress(long progress, long size) {
        return (int) (((double) progress / (double) size) * 100);
    }
}
