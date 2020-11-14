package com.nobodyknows.chatlistlayoutview.Services;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import com.nobodyknows.chatlinkpreview.ChatLinkView;
import com.nobodyknows.chatlinkpreview.ChatViewListener;
import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;
import com.nobodyknows.chatlinkpreview.MetaData;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.circularprogressbutton.ProgressButton;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.Model.Contact;
import com.nobodyknows.commonhelper.Model.ContactParceable;
import com.nobodyknows.commonhelper.Model.SharedFile;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.VISIBLE;

public class LayoutService {
    public static DownloadManager downloadManager;
    public static void checkForLink(ChatLinkView chatLinkView, TextView message, ChatLinkDatabaseHelper chatLinkDatabaseHelper) {
        URLSpan span[] = message.getUrls();
        if(span.length > 0) {
            String link = span[0].getURL();
            if(link != null && link.length() > 0) {
                chatLinkView.setChatLinkDatabaseHelper(chatLinkDatabaseHelper);
                chatLinkView.setFromLink(link, new ChatViewListener() {
                    @Override
                    public void onSuccess(MetaData metaData) {
                        chatLinkView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }
    }

    public static void initDownloadManager(Context context) {
        downloadManager = DownloadService.getDownloadManager(context);
    }

    public static DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public static String getFormatedDate(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String currentTime = sdf.format(date);
        return currentTime.toUpperCase();
    }

    public static void updateMessageStatus(MessageStatus status, ImageView imageView) {
        if(status == MessageStatus.SEEN) {
            imageView.setImageResource(R.drawable.seen);
        } else if(status == MessageStatus.RECEIVED) {
            imageView.setImageResource(R.drawable.received);
        }  else if(status == MessageStatus.SENT) {
            imageView.setImageResource(R.drawable.sent);
        } else if(status == MessageStatus.SENDING) {
            imageView.setImageResource(R.drawable.waiting); //TODO Change icon
        }
    }

    public static boolean containsURL(String content){
        String REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        if(m.find()) {
            return true;
        }

        return false;
    }

    public static String getSize(double size) {
        String hrSize = null;
        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);
        DecimalFormat dec = new DecimalFormat("0.00");
        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" B");
        }
        return "Size : "+hrSize;
    }

    public static String getDuration(Double timeInMillis) {
        int hours = (int) ((timeInMillis / (1000 * 60 * 60)));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        int seconds = (int) ((timeInMillis / 1000) % 60);
        String time = "0";
        if (hours == 0) {
            time = minutes + ":" + seconds;
        } else {
            time = hours + ":" + minutes + ":" + seconds;
        }
        return time;
    }

    public static Boolean canShowDownloadButton(String downloadPath, ArrayList<SharedFile> sharedFiles) {
        String envPath = Environment.getExternalStorageDirectory().getPath();
        Boolean canShow = false;
        for(SharedFile sharedFile:sharedFiles) {
            if(!new File(envPath+downloadPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).exists()) {
                canShow = true;
                break;
            }
        }
        return canShow;
    }

    public static ArrayList<ContactParceable> getParceableList(ArrayList<Contact> contacts) {
        ContactParceable contactParceable;
        ArrayList<ContactParceable> parceableArrayList = new ArrayList<>();
        for(Contact contact:contacts) {
            contactParceable = new ContactParceable(contact.getName(),contact.getContactNumbers());
            parceableArrayList.add(contactParceable);
        }
        return parceableArrayList;
    }

    public static void downloadFiles(Context context,ArrayList<SharedFile> sharedFiles, String downloadPath, ProgressButton progressButton,String messageId) {
        if(sharedFiles.size() == 1) {
            downloadSingle(sharedFiles.get(0),downloadPath,progressButton,messageId,context);
        } else {
            downloadAll(sharedFiles,downloadPath,progressButton,messageId,context);
        }
    }

    public static void downloadAll(ArrayList<SharedFile> urls, String dirPath, ProgressButton progressBar, String messageId, Context context) {
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
                                }
                                if(progress == 100.0) {
                                }
                                renameFile(partialUrl,realname);
                            }

                            @Override
                            public void onDownloadFailed(DownloadException e) {
                                downloadManager.remove(downloadInfo);
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
                progressBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(DownloadInfo downloadInfo:downloadInfos) {
                            downloadManager.pause(downloadInfo);
                        }
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

    public static void downloadSingle(SharedFile sharedFile, String dirPath, ProgressButton progressBar, String messageId, Context context) {
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
                            renameFile(partialUrl,envPath+dirPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension());
                        }

                        @Override
                        public void onDownloadFailed(DownloadException e) {
                            downloadManager.remove(downloadInfo);
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

    private static void renameFile(String partialUrl, String newUrl) {
        File from = new File(partialUrl);
        File to = new File(newUrl);
        if(from.exists()) {
            from.renameTo(to);
        }
    }

    private static int calculateProgress(long progress, long size) {
        return (int) (((double) progress / (double) size) * 100);
    }
}
