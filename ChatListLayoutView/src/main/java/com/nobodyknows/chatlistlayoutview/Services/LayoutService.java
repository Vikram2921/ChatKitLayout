package com.nobodyknows.chatlistlayoutview.Services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import com.nobodyknows.circularprogressbutton.ProgressClickListener;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.nobodyknows.commonhelper.Model.Contact;
import com.nobodyknows.commonhelper.Model.ContactParceable;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.SharedFile;
import com.nobodyknows.commonhelper.Services.UploadAndDownloadViewHandler;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.VISIBLE;
import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.chatLayoutListener;
import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.downloadPaths;
import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.myId;

public class LayoutService {
    public static DownloadManager downloadManager;
    public static ChatLinkDatabaseHelper chatLinkDatabaseHelper;
    private static SeekBar LseekBar;
    private static ImageView LplayPause;
    private static String LmessageId;
    private static MediaPlayer mediaPlayer;
    private static MediaObserver observer;
    private static Map<String,ArrayList<DownloadInfo>> downloadInfos;
    private static UploadAndDownloadViewHandler uploadAndDownloadViewHandler;
    public static void checkForLink(ChatLinkView chatLinkView, TextView message) {
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


    public static void initialize(Context context) {
        chatLinkDatabaseHelper = new ChatLinkDatabaseHelper(context);
        downloadManager = DownloadService.getDownloadManager(context);
        LmessageId = "";
        LseekBar = null;
        LplayPause = null;
        mediaPlayer = new MediaPlayer();
        downloadInfos = new HashMap<>();
        uploadAndDownloadViewHandler = new UploadAndDownloadViewHandler(context);
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

    public static Boolean canShowDownloadButton(MessageType messageType, ArrayList<SharedFile> sharedFiles) {
        String envPath = Environment.getExternalStorageDirectory().getPath();
        Boolean canShow = false;
        for(SharedFile sharedFile:sharedFiles) {
            if(!new File(envPath+downloadPaths.get(messageType)+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).exists()) {
                canShow = true;
                break;
            }
        }
        return canShow;
    }

    public static String getFullFileUrl(String downloadPath,SharedFile sharedFile) {
        return Environment.getExternalStorageDirectory().getPath()+downloadPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension();
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

    public static void downloadFiles(Context context,ArrayList<SharedFile> sharedFiles, MessageType messageType, ProgressButton progressButton,String messageId) {
        downloadInfos.put(messageId,new ArrayList<>());
        if(sharedFiles.size() == 1) {
            downloadSingle(sharedFiles.get(0),messageType,progressButton,messageId,context);
        } else {
            downloadAll(sharedFiles,messageType,progressButton,messageId,context);
        }
    }

    private static void downloadAll(ArrayList<SharedFile> urls, MessageType messageType, ProgressButton progressBar, String messageId, Context context) {
        String dirPath = downloadPaths.get(messageType);
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String envPath = Environment.getExternalStorageDirectory().getPath();
                if (!new File(envPath +dirPath).exists()) {
                    new File(envPath + dirPath).mkdirs();
                }
                int totalDownloads = urls.size();
                final int[] downloadCompleted = {0};
                for(int i=0;i<urls.size();i++) {
                    String partialUrl = envPath+dirPath+"/"+urls.get(i).getName()+"_PARTIALLY."+urls.get(i).getExtension();
                    String realname = envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension();
                    if (new File(partialUrl).exists()) {
                        new File(partialUrl).delete();
                    }
                    if(!new File(envPath+dirPath+"/"+urls.get(i).getName()+"."+urls.get(i).getExtension()).exists()) {
                        DownloadInfo downloadInfo  = new DownloadInfo.Builder().setUrl(urls.get(i).getUrl()).setPath(partialUrl).build();
                        int finalTotalDownloads = totalDownloads;
                        int finalI = i;
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
                                    progressBar.setVisibility(View.GONE);
                                    downloadInfos.remove(messageId);
                                }
                                renameFile(partialUrl,realname);
                            }

                            @Override
                            public void onDownloadFailed(DownloadException e) {
                                downloadManager.remove(downloadInfo);
                                downloadInfos.get(messageId).remove(finalI);
                                downloadInfos.get(messageId).add(finalI,null);
                            }
                        });
                        downloadInfos.get(messageId).add(i,downloadInfo);
                        downloadManager.download(downloadInfo);
                    } else {
                        downloadCompleted[0]++;
                        double progress = (((double)downloadCompleted[0]/(double)totalDownloads)*100);
                        progressBar.setProgress((float) progress);
                        downloadInfos.get(messageId).add(i,null);
                        if(progress == 100.0) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
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

    private static void downloadSingle(SharedFile sharedFile, MessageType messageType, ProgressButton progressBar, String messageId, Context context) {
        String dirPath = downloadPaths.get(messageType);
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
                            progressBar.setVisibility(View.GONE);
                            downloadInfos.remove(messageId);
                        }

                        @Override
                        public void onDownloadFailed(DownloadException e) {
                            downloadManager.remove(downloadInfo);
                            downloadInfos.remove(messageId);
                            progressBar.resetProgressButton();
                        }
                    });
                    downloadInfos.get(messageId).add(downloadInfo);
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

    public static ChatLinkDatabaseHelper getChatLinkDatabaseHelper() {
        return chatLinkDatabaseHelper;
    }

    private static class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                LseekBar.setProgress((int)((double)mediaPlayer.getCurrentPosition() / (double)mediaPlayer.getDuration()*100));
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                }

            }
        }
    }

    private static void playeAudio(Context context,String messageId,String url,SeekBar seekBar,ImageView playPause) {
        if(messageId == null || messageId.length()  == 0) {
            LmessageId = messageId;
            LseekBar = seekBar;
            LplayPause = playPause;
            playMusic(context,url);
        } else {
            if(LmessageId.equals(messageId)) {
                mediaPlayer.start();
            } else {
                stopAudio();
                mediaPlayer.reset();
                if(LseekBar != null) {
                    LseekBar.setProgress(0);
                    LseekBar.setSecondaryProgress(0);
                }
                LmessageId = messageId;
                LseekBar = seekBar;
                LplayPause = playPause;
                playMusic(context,url);
            }
        }
    }

    private static void stopAudio() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            LplayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private static String getLastPlayingMessageId() {
        return LmessageId;
    }

    private static boolean isPlayingAudio() {
        return mediaPlayer.isPlaying();
    }

    private static void onSeekBarProgressChange(int progress) {
        mediaPlayer.seekTo(progress);
    }
    private static void playMusic(Context context,String url) {
        LplayPause.setImageResource(R.drawable.ic_baseline_pause_24);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(context, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        observer.stop();
                        LseekBar.setProgress(0);
                        LseekBar.setSecondaryProgress(0);
                        LplayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                });
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        LseekBar.setSecondaryProgress(percent);
                    }
                });
                observer = new MediaObserver();
                new Thread(observer).start();
            }
        });
        mediaPlayer.prepareAsync();
    }

    public static void initAudioPlayerView(Context context,Message message,ImageView playPause,SeekBar seekBar) {
        if(LmessageId.equals(message.getMessageId())) {
            seekBar.setProgress(LseekBar.getProgress());
            seekBar.setSecondaryProgress(LseekBar.getSecondaryProgress());
            if(isPlayingAudio()) {
                playPause.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        }
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLastPlayingMessageId().equals(message.getMessageId()) && isPlayingAudio()) {
                    stopAudio();
                } else {
                    String url = message.getSharedFiles().get(0).getUrl();
                    if(!canShowDownloadButton(message.getMessageType(),message.getSharedFiles())) {
                        url = getFullFileUrl(downloadPaths.get(message.getMessageType()),message.getSharedFiles().get(0));
                    }
                    playeAudio(context,message.getMessageId(),url,seekBar,playPause);
                }
            }
        });
    }

    public static void destroyPlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.reset();
        }
        if(observer != null) {
            observer.stop();
        }
    }

    public static void configureMessageView(Message message,View view,Boolean isReceived) {
        if(message.getMessageType() != MessageType.STICKER) {
            view.findViewById(R.id.rootBox).setBackgroundResource(message.getMessageConfiguration().getBackgroundResource());
        } else {
            if(isReceived) {
                view.findViewById(R.id.sendername).setBackgroundResource(message.getMessageConfiguration().getBackgroundResource());
                view.findViewById(R.id.messagetime).setBackgroundResource(message.getMessageConfiguration().getBackgroundResource());
            } else {
                view.findViewById(R.id.rootBox).setBackgroundResource(message.getMessageConfiguration().getBackgroundResource());
            }
        }
    }

    public static void handlerDownloadAndUploadCase(Context context,View view,Message message) {
        ProgressButton progressButton = view.findViewById(R.id.probutton);
        progressButton.initalize();
        if(message.getSender().equals(myId)) {
            if(message.getMessageStatus() == MessageStatus.SENDING) {
                progressButton.setVisibility(VISIBLE);
                progressButton.setUploadType();
                progressButton.setProgressClickListener(new ProgressClickListener() {
                    @Override
                    public void onStart() {
                        chatLayoutListener.onUploadRetry(message);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {
                progressButton.setVisibility(View.GONE);
            }
        } else {
            if(canShowDownloadButton(message.getMessageType(),message.getSharedFiles())) {
                progressButton.setVisibility(VISIBLE);
                progressButton.setDownloadType();
                progressButton.setProgressClickListener(new ProgressClickListener() {
                    @Override
                    public void onStart() {
                        downloadFiles(context,message.getSharedFiles(),message.getMessageType(),progressButton,message.getMessageId());
                    }

                    @Override
                    public void onCancel() {
                        cancelDownload(message.getMessageId());
                    }
                });
            }
        }
    }

    private static void cancelDownload(String messageId) {
        for(DownloadInfo downloadInfo : downloadInfos.get(messageId)) {
            if(downloadInfo != null) {
                downloadManager.pause(downloadInfo);
                downloadManager.remove(downloadInfo);
            }
        }
        downloadInfos.remove(messageId);
    }

    public static void changeToGalleryIntent(Context context,Message message) {
//        Intent intent = new Intent(context, Gallery.class);
//        intent.putExtra("type",message.getMessageType().name());
//        intent.putStringArrayListExtra("urls",LayoutService.getUrlsFromSharedFiles(message.getSharedFiles()));
//        context.startActivity(intent);
    }

    private static ArrayList<String> getUrlsFromSharedFiles(ArrayList<SharedFile> sharedFiles) {
        ArrayList<String> urls = new ArrayList<>();
        for(SharedFile sharedFile:sharedFiles) {
            urls.add(sharedFile.getUrl());
        }
        return urls;
    }

    //Get Upload Views for while uploading
    private static void getUploadView(Message message) {

    }


}
