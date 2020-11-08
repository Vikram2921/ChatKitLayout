package com.nobodyknows.chatlayoutview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nobodyknows.chatlayoutview.Model.SharedFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.nobodyknows.chatlayoutview.ChatLayoutView.uploadAndDownloadViewHandler;

public class DownloadHelper {
    private Context context;
    private DownloadManager downloadManager;
    public DownloadHelper(Context context) {
        this.context =context;
        downloadManager = DownloadService.getDownloadManager(context);
    }


    public void downloadAll(ArrayList<SharedFile> urls, String dirPath, CircularProgressBar progressBar, String messageId, RelativeLayout progressview, View imageup) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String envPath = Environment.getExternalStorageDirectory().getPath();

                if (!new File(envPath +dirPath).exists()) {
                    new File(envPath + dirPath).mkdirs();
                }
                int totalDownloads = urls.size();
                final int[] downloadCompleted = {0};
                String filename = "";
                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                for(int i=0;i<urls.size();i++) {
                    String partialUrl = envPath+dirPath+"/"+urls.get(i).getName()+"_PARTIALLY."+urls.get(i).getExtension();
                    String realname = envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension();
                    if (new File(partialUrl).exists()) {
                        new File(partialUrl).delete();
                    }
                    if(!new File(envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension()).exists()) {
                        DownloadInfo downloadInfo  = new DownloadInfo.Builder().setUrl(urls.get(i).getUrl()).setPath(partialUrl).build();
                        downloadInfos.add(downloadInfo);
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
                                double progress = (((double)downloadCompleted[0]/(double)finalTotalDownloads)*100);
                                if(progress > 0) {
                                    progressBar.setIndeterminateMode(false);
                                    progressBar.setProgress((float) progress);
                                }
                                if(progress == 100.0) {
                                    progressview.setVisibility(View.GONE);
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
                        int progress = ((downloadCompleted[0] /totalDownloads)*100);
                        progressBar.setProgress(progress);
                        if(progress == 100.0) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
                progressview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(DownloadInfo downloadInfo:downloadInfos) {
                            downloadManager.pause(downloadInfo);
                        }
                        imageup.setVisibility(View.VISIBLE);
                        progressview.setVisibility(View.GONE);
                        uploadAndDownloadViewHandler.delete(messageId);
                    }
                });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    public void downloadSingle(SharedFile sharedFile, String dirPath, CircularProgressBar progressBar, String messageId, RelativeLayout progressview, View imageup) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
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
                            int progressDone = calculateProgress(progress,size);
                            if(progressDone > 0) {
                                progressBar.setIndeterminateMode(false);
                                progressBar.setProgress(progressDone);
                            }
                        }

                        @Override
                        public void onRemoved() {

                        }

                        @Override
                        public void onDownloadSuccess() {
                            progressview.setVisibility(View.GONE);
                            uploadAndDownloadViewHandler.delete(messageId);
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

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
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
