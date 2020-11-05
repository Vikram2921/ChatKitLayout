package com.nobodyknows.chatlayoutview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.nobodyknows.chatlayoutview.Model.SharedFile;

import java.io.File;
import java.util.ArrayList;

import static com.nobodyknows.chatlayoutview.ChatLayoutView.uploadAndDownloadViewHandler;

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

    public int downloadAll(ArrayList<SharedFile> urls, String dirPath, ProgressBar progressBar, String messageId, RelativeLayout progressview, ImageView imageup) {
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
                String partialUrl = envPath+dirPath+"/"+urls.get(i).getName()+"_PARTIALLY."+urls.get(i).getExtension();
                String realname = envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension();
                if (new File(partialUrl).exists()) {
                    new File(partialUrl).delete();
                }
                if(!new File(envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension()).exists()) {
                    DownloadInfo downloadInfo  = new DownloadInfo.Builder().setUrl(urls.get(i).getUrl()).setPath(partialUrl).build();
                    int finalTotalDownloads = totalDownloads;
                    downloadInfo.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onStart() {
                            progressBar.setIndeterminate(false);
                            progressview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageup.setVisibility(View.VISIBLE);
                                    progressview.setVisibility(View.GONE);
                                    downloadManager.pause(downloadInfo);
                                    uploadAndDownloadViewHandler.delete(messageId);
                                }
                            });
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
                            progressBar.setProgress((int) progress);
                            if(progress == 100.0) {
                                progressBar.setVisibility(View.GONE);
                                uploadAndDownloadViewHandler.delete(messageId);
                            }
                            renameFile(partialUrl,realname);
                        }

                        @Override
                        public void onDownloadFailed(DownloadException e) {
                            imageup.setVisibility(View.VISIBLE);
                            progressview.setVisibility(View.GONE);
                            downloadManager.remove(downloadInfo);
                            uploadAndDownloadViewHandler.delete(messageId);
                        }
                    });
                    downloadManager.download(downloadInfo);
                } else {
                    downloadCompleted[0]++;
                    float progress = ((float) downloadCompleted[0] / (float) totalDownloads)*100;
                    progressBar.setProgress((int) progress);
                    if(progress == 100.0) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }
        return downloadId;
    }

    public int downloadSingle(SharedFile sharedFile, String dirPath, ProgressBar progressBar, String messageId, RelativeLayout progressview, ImageView imageup) {
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
            String partialUrl = envPath+dirPath+"/"+sharedFile.getName()+"_PARTIALLY."+sharedFile.getExtension();
            if (new File(partialUrl).exists()) {
                new File(partialUrl).delete();
            }
            if (!new File(envPath +dirPath).exists()) {
                new File(envPath + dirPath).mkdirs();
            }

            if(!new File(envPath+dirPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).exists()) {
                DownloadInfo downloadInfo  = new DownloadInfo.Builder().setUrl(sharedFile.getUrl()).setPath(partialUrl).build();
                downloadInfo.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onStart() {
                        progressBar.setIndeterminate(false);
                        progressBar.setMax(100);
                        progressview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageup.setVisibility(View.VISIBLE);
                                progressview.setVisibility(View.GONE);
                                downloadManager.pause(downloadInfo);
                                uploadAndDownloadViewHandler.delete(messageId);
                            }
                        });
                    }

                    @Override
                    public void onWaited() {

                    }

                    @Override
                    public void onPaused() {

                    }

                    @Override
                    public void onDownloading(long progress, long size) {

                        Toast.makeText(context,calculateProgress(progress,size)+"",Toast.LENGTH_SHORT).show();
                        progressBar.setProgress(calculateProgress(progress,size));
                    }

                    @Override
                    public void onRemoved() {

                    }

                    @Override
                    public void onDownloadSuccess() {
                        progressBar.setVisibility(View.GONE);
                        uploadAndDownloadViewHandler.delete(messageId);
                        Toast.makeText(context,uploadAndDownloadViewHandler.isExist(messageId)+"",Toast.LENGTH_SHORT).show();
                        renameFile(partialUrl,envPath+dirPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension());
                    }

                    @Override
                    public void onDownloadFailed(DownloadException e) {
                        imageup.setVisibility(View.VISIBLE);
                        progressview.setVisibility(View.GONE);
                        downloadManager.remove(downloadInfo);
                        uploadAndDownloadViewHandler.delete(messageId);
                    }
                });
                downloadManager.download(downloadInfo);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
        return downloadId;
    }

    private void renameFile(String partialUrl, String newUrl) {
        File from = new File(partialUrl);
        File to = new File(newUrl);
        if(from.exists()) {
            from.renameTo(to);
        }
    }

    private int calculateProgress(long progress, long size) {
        return (int) (((double) progress / (double) size) * 100);
    }
}
