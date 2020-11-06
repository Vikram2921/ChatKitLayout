package com.nobodyknows.chatlayoutview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nobodyknows.chatlayoutview.Activities.viewmedia;
import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageStatus;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.SharedFile;
import com.nobodyknows.chatlayoutview.Model.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nobodyknows.chatlayoutview.ChatLayoutView.chatLayoutListener;
import static com.nobodyknows.chatlayoutview.ChatLayoutView.helper;
import static com.nobodyknows.chatlayoutview.ChatLayoutView.lastPlayingAudioMessageId;
import static com.nobodyknows.chatlayoutview.ChatLayoutView.lastPlayingDuration;
import static com.nobodyknows.chatlayoutview.ChatLayoutView.lastPlayingImageView;
import static com.nobodyknows.chatlayoutview.ChatLayoutView.lastPlayingProgressBar;
import static com.nobodyknows.chatlayoutview.ChatLayoutView.uploadAndDownloadViewHandler;

public class ChatMessageView extends RelativeLayout implements MediaPlayer.OnPreparedListener {
    private LayoutInflater layoutInflater;
    private RelativeLayout root,rootview,messageBox,dateview;
    private TextView message,messageTime,senderName;
    private ImageView messagestatus;
    private User user;
    private String downloadPath;
    private String envPath = Environment.getExternalStorageDirectory().getPath();
    private DownloadHelper downloadHelper;
    private LinearLayout customView,innerView;
    private Message currentMessage;
    private String DOT_SEPRATOR = " \u25CF ";
    private int STICKER_SIZE = 400;
    private MediaPlayer mediaPlayer;
    private String DELTE_MESSAGE = "This message was deleted";
    private MediaObserver observer = null;
    private SeekBar progressBar;
    private TextView duration;

    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                progressBar.setProgress((int)((double)mediaPlayer.getCurrentPosition() / (double)mediaPlayer.getDuration()*100));
               // duration.setText(mediaPlayer.getCurrentPosition()+" / "+mediaPlayer.getDuration());
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                }

            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                observer.stop();
                progressBar.setProgress(mp.getCurrentPosition());
                mediaPlayer.stop();
                mediaPlayer.reset();
                lastPlayingImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                lastPlayingProgressBar.setProgress(0);
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                progressBar.setSecondaryProgress(percent);
            }
        });
        if(observer == null) {
            observer = new MediaObserver();
        }
        new Thread(observer).start();
    }



    public ChatMessageView(Context context) {
        super(context);
        init(null,0);
    }

    public ChatMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ChatMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = (RelativeLayout) layoutInflater.inflate(R.layout.messageview,this,true);
        rootview = root.findViewById(R.id.rootview);
        dateview = root.findViewById(R.id.dateView);
        message = root.findViewById(R.id.message);
        innerView = root.findViewById(R.id.datetimeview);
        senderName = root.findViewById(R.id.sendername);
        customView = root.findViewById(R.id.customviews);
        messageBox = root.findViewById(R.id.messagebox);
        messagestatus = root.findViewById(R.id.messagestatus);
        messageTime = root.findViewById(R.id.messagetime);

    }

    private void configRootView(MessagePosition position) {
        RelativeLayout.LayoutParams params = (LayoutParams) rootview.getLayoutParams();
        if(position == MessagePosition.LEFT) {
            params.setMargins(0,0,100,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            this.messagestatus.setVisibility(GONE);
        } else if(position == MessagePosition.RIGHT) {
            senderName.setVisibility(GONE);
            params.setMargins(100,0,0,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        messageBox.setBackgroundResource(currentMessage.getMessageConfiguration().getBackgroundResource());
        rootview.setLayoutParams(params);
    }

    private String getMessageTime(Message message) {
        long size = 0;
        for(SharedFile sharedFile:message.getSharedFiles()) {
            size += sharedFile.getSize();
        }
        String time= getFormatedDate("hh:mm aa", message.getSentAt());
        if(message.getMessageType() == MessageType.AUDIO) {
            time+=DOT_SEPRATOR+"Audio "+DOT_SEPRATOR+helper.getSize(size);
        } else if(message.getMessageType() == MessageType.VIDEO) {
            time+=DOT_SEPRATOR+"Video "+DOT_SEPRATOR+helper.getSize(size);
        }  else if(message.getMessageType() == MessageType.IMAGE) {
            time+=DOT_SEPRATOR+"Image "+DOT_SEPRATOR+helper.getSize(size);
        }  else if(message.getMessageType() == MessageType.GIF) {
            time+=DOT_SEPRATOR+"Gif ";
        }  else if(message.getMessageType() == MessageType.DOCUMENT) {
            time+=DOT_SEPRATOR+"Document "+DOT_SEPRATOR+helper.getSize(size);
        }  else if(message.getMessageType() == MessageType.RECORDING) {
            time+=DOT_SEPRATOR+"Recording "+DOT_SEPRATOR+helper.getSize(size);
        }  else if(message.getMessageType() == MessageType.CONTACT) {
            time+=DOT_SEPRATOR+"Contacts ";
        }
       return time;
    }


    public void setMessage(Message message) {
        this.currentMessage = message;
        if(message.getMessageType() == MessageType.DATE) {
            TextView textView = root.findViewById(R.id.date);
            textView.setText(message.getMessage());
            this.dateview.setVisibility(VISIBLE);
            this.rootview.setVisibility(GONE);
        } else {
            this.message.setTextSize(message.getMessageConfiguration().getMessageTextSize());
            this.message.setTextColor(message.getMessageConfiguration().getTextColor());
            this.messageTime.setTextColor(message.getMessageConfiguration().getTimeTextColor());
            configRootView(message.getMessageConfiguration().getMessagePosition());
            if(message.getMessageStatus() != MessageStatus.DELETED) {
                updateStickerView(message.getMessageConfiguration().getMessagePosition());
                this.message.setText(message.getMessage());
                this.messageTime.setText(getMessageTime(message));
                updateMessageStatus(message);
                if(message.getIsRepliedMessage()) {
                    ViewGroup parentGroup = ((ViewGroup) message.getReplyMessageView().getParent());
                    if(parentGroup != null) {
                        parentGroup.removeView(message.getReplyMessageView());
                    }
                    customView.addView(message.getReplyMessageView());
                    customView.setVisibility(VISIBLE);
                }
                if (message.getMessageType() != MessageType.TEXT) {
                    updateMessageView(message);
                }

            } else {
                this.message.setText(DELTE_MESSAGE);
                senderName.setVisibility(GONE);
                innerView.setVisibility(GONE);
                if(message.getMessageConfiguration().getMessagePosition() == MessagePosition.LEFT) {
                    this.message.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_block_24,0);
                } else {
                    this.message.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_block_24,0,0,0);
                }
                this.message.setCompoundDrawablePadding(10);
            }
        }
    }

    private void updateStickerView(MessagePosition position) {
        if(currentMessage.getMessageType() == MessageType.STICKER && currentMessage.getMessageStatus() != MessageStatus.DELETED) {
            LayoutParams layoutParams = new LayoutParams(STICKER_SIZE,LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW,R.id.sendername);
            innerView.setBackgroundResource(currentMessage.getMessageConfiguration().getBackgroundResource());
            senderName.setBackgroundResource(currentMessage.getMessageConfiguration().getBackgroundResource());
            if(position == MessagePosition.LEFT) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else if(position == MessagePosition.RIGHT) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                LayoutParams layoutParamsinnerview = (LayoutParams) innerView.getLayoutParams();
                layoutParamsinnerview.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                innerView.setLayoutParams(layoutParamsinnerview);
                LayoutParams layoutParamsName = (LayoutParams) senderName.getLayoutParams();
                layoutParamsName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                senderName.setLayoutParams(layoutParamsName);
            }
            customView.setLayoutParams(layoutParams);
            if(!currentMessage.getIsRepliedMessage()) {
                messageBox.setBackgroundResource(0);
            } else {
                if(position == MessagePosition.RIGHT) {
                    LayoutParams layoutParamsinnerview = (LayoutParams) messageBox.getLayoutParams();
                    layoutParamsinnerview.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParamsinnerview.width = STICKER_SIZE;
                    messageBox.setLayoutParams(layoutParamsinnerview);
                }
            }
            message.setVisibility(GONE);
        }
    }

    private View getMediaView(SharedFile sharedFile, int height, Boolean showOverlay, String overlayText, ArrayList<String> urls,ArrayList<String> names) {
        View view = layoutInflater.inflate(R.layout.image_view,null);
        ImageView imageView = view.findViewById(R.id.image);
        ImageView playIcon = view.findViewById(R.id.playIcon);
        if(showOverlay) {
            RelativeLayout overlay = view.findViewById(R.id.overlay);
            TextView textView = view.findViewById(R.id.overlaytext);
            textView.setText(overlayText);
            overlay.setVisibility(VISIBLE);
        }
        String url = sharedFile.getPreviewUrl();
        if(url == null || url.length() == 0) {
            url = sharedFile.getUrl();
        }
        if(currentMessage.getMessageType() == MessageType.VIDEO) {
            imageView.setVisibility(VISIBLE);
            playIcon.setVisibility(VISIBLE);
            Glide.with(getContext()).load(url).override(150,150).into(imageView);
        } else if(currentMessage.getMessageType() == MessageType.GIF) {
            playIcon.setVisibility(VISIBLE);
            playIcon.setImageResource(R.drawable.ic_baseline_gif_24);
            Glide.with(getContext()).asBitmap().load(url).override(200, 200).into(imageView);
        } else if(currentMessage.getMessageType() == MessageType.STICKER) {
            playIcon.setVisibility(GONE);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(15,15,15,15);
            LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
            layoutParams.width = STICKER_SIZE;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageView.setLayoutParams(layoutParams);
            Glide.with(getContext()).load(url).override(200, 200).into(imageView);
        } else {
            Glide.with(getContext()).load(url).override(150,150).into(imageView);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, height);
        layoutParams.weight =1;
        layoutParams.setMargins(3,3,3,3);
        view.setLayoutParams(layoutParams);
        if(currentMessage.getMessageType() != MessageType.STICKER) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), viewmedia.class);
                    intent.putStringArrayListExtra("urls",urls);
                    intent.putStringArrayListExtra("names",names);
                    intent.putExtra("clicked",sharedFile.getUrl());
                    intent.putExtra("type",currentMessage.getMessageType().ordinal());
                    intent.putExtra("localpath",envPath+downloadPath+"/");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });
        }
        return view;
    }

    private int getHeightByUrlsSize(int size) {
        if(size >= 4) {
            return 300;
        } else if(size == 3) {
            return 400;
        }  else if(size == 2)  {
            return 500;
        } else {
            return 600;
        }
    }

    private Boolean canShowDownloadButton(String downloadPath,ArrayList<SharedFile> sharedFiles) {
        Boolean canShow = false;

        for(SharedFile sharedFile:sharedFiles) {
            if(!new File(envPath+downloadPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).exists()) {
                canShow = true;
                break;
            }
        }
        return canShow;
    }

    private Boolean canShowDownloadButton(String downloadPath,SharedFile sharedFile) {
        Boolean canShow = false;
        if(!new File(envPath+downloadPath+"/"+sharedFile.getName()+"."+sharedFile.getExtension()).exists()) {
            canShow = true;
        }
        return canShow;
    }

    private View getMediaLayout(ArrayList<SharedFile> sharedFiles,MessageType messageType) {

        View view = null;
        if(uploadAndDownloadViewHandler.isExist(currentMessage.getMessageId())) {
            view = uploadAndDownloadViewHandler.getUploadView(currentMessage.getMessageId()).getView();
        } else {
            view = layoutInflater.inflate(R.layout.image_chat_view,null);
            LinearLayout line1 = view.findViewById(R.id.line1);
            LinearLayout line2 = view.findViewById(R.id.line2);
            ImageView imageup = view.findViewById(R.id.imageup);
            RelativeLayout progressview = view.findViewById(R.id.progressview);
            CircularProgressBar eventButtton = view.findViewById(R.id.progressbutton);
            ArrayList<String> urls = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            for(SharedFile sharedFile:sharedFiles) {
                urls.add(sharedFile.getUrl());
                names.add(sharedFile.getName()+"."+sharedFile.getExtension());
            }
            int height = getHeightByUrlsSize(sharedFiles.size());
            for(int i=0;i<sharedFiles.size();i++) {
                if(i<2) {
                    line1.addView(getMediaView(sharedFiles.get(i),height,false,"",urls,names));
                } else {
                    if(i == 3) {
                        if(sharedFiles.size() > 4) {
                            line2.addView(getMediaView(sharedFiles.get(i),height,true,"+ "+(sharedFiles.size()-4),urls,names));
                            break;
                        } else {
                            line2.addView(getMediaView(sharedFiles.get(i),height,false,"",urls,names));
                        }
                    } else {
                        line2.addView(getMediaView(sharedFiles.get(i),height,false,"",urls,names));
                    }
                }
            }
            if(messageType == MessageType.GIF || messageType == MessageType.STICKER) {
                progressview.setVisibility(GONE);
            } else {
                Boolean canDownload = canShowDownloadButton(downloadPath,sharedFiles);
                if(canDownload) {
                    imageup.setVisibility(VISIBLE);
                    if(currentMessage.getMessageConfiguration().getMediaAutoDownload()) {
                        downloadFiles(imageup,progressview,sharedFiles,view,eventButtton);
                    } else {
                        View finalView = view;
                        eventButtton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFiles(imageup,progressview,sharedFiles,finalView,eventButtton);
                            }
                        });
                    }
                } else {
                    imageup.setVisibility(GONE);
                }
            }

        }
        return view;
    }

    private void downloadFiles(ImageView imageup, RelativeLayout progressview, ArrayList<SharedFile> sharedFiles, View view, CircularProgressBar progressBar) {
        imageup.setVisibility(GONE);
        progressview.setVisibility(VISIBLE);
        uploadAndDownloadViewHandler.addUploadView(currentMessage, view);
        if(sharedFiles.size() == 1) {
            downloadHelper.downloadSingle(sharedFiles.get(0),downloadPath,progressBar,currentMessage.getMessageId(), progressview, imageup);
        } else {
            downloadHelper.downloadAll(sharedFiles,downloadPath,progressBar,currentMessage.getMessageId(),progressview,imageup);
        }
    }


    private void updateMessageView(Message message) {
        customView.setVisibility(VISIBLE);
        switch (message.getMessageType())
        {
            case IMAGE:
                customView.addView(getMediaLayout(message.getSharedFiles(),MessageType.IMAGE));
                break;
            case VIDEO:
                customView.addView(getMediaLayout(message.getSharedFiles(),MessageType.VIDEO));
                break;
            case AUDIO:
                customView.addView(getAudioLayout(message.getSharedFiles()));
                break;
            case RECORDING:
                customView.addView(getRecordingLayout(message.getSharedFiles()));
                break;
            case GIF:
                customView.addView(getMediaLayout(message.getSharedFiles(),MessageType.GIF));
                break;
            case STICKER:
                customView.addView(getMediaLayout(message.getSharedFiles(),MessageType.STICKER));
                break;
            case DOCUMENT:
                customView.addView(getDocumentLayout(message.getSharedFiles()));
                break;
            default:
                break;
        }
    }

    private View getDocumentLayout(ArrayList<SharedFile> sharedFiles) {
        View view = null;
        message.setVisibility(GONE);
        if(uploadAndDownloadViewHandler.isExist(currentMessage.getMessageId())) {
            view = uploadAndDownloadViewHandler.getUploadView(currentMessage.getMessageId()).getView();
        } else {
            view = layoutInflater.inflate(R.layout.document_view,null);
            ImageView fileicon = view.findViewById(R.id.fileicon);
            ImageView imageup = view.findViewById(R.id.imageup);
            TextView filename = view.findViewById(R.id.documentname);
            RoundedImageView preview = view.findViewById(R.id.preview_file);
            TextView fileinfo = view.findViewById(R.id.documentinfo);
            RelativeLayout progressview = view.findViewById(R.id.progressview);
            RelativeLayout box = view.findViewById(R.id.box);
            CircularProgressBar eventButtton = view.findViewById(R.id.progressbutton);
            Boolean canDownload = canShowDownloadButton(downloadPath,sharedFiles);
            filename.setText(sharedFiles.get(0).getName());
            int resId = getResources().getIdentifier(sharedFiles.get(0).getExtension(),"drawable",getContext().getPackageName());
            Log.d("TAGRESID", "getDocumentLayout: "+resId);
            if(resId != 0){
                fileicon.setImageResource(resId);
            }
            if(sharedFiles.get(0).getPreviewUrl() != null && sharedFiles.get(0).getPreviewUrl().length() > 0 ){
                preview.setVisibility(VISIBLE);
                Glide.with(getContext()).load(sharedFiles.get(0).getPreviewUrl()).into(preview);
            }
            if(canDownload) {
                box.setClickable(false);
                imageup.setVisibility(VISIBLE);
                if(currentMessage.getMessageConfiguration().getMediaAutoDownload()) {
                    downloadFiles(imageup,progressview,sharedFiles,view,eventButtton);
                } else {
                    View finalView = view;
                    imageup.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadFiles(imageup,progressview,sharedFiles,finalView,eventButtton);
                        }
                    });
                }
            } else {
                box.setClickable(true);
                imageup.setVisibility(GONE);
            }
        }
        return view;
    }

    private View getAudioLayout(ArrayList<SharedFile> sharedFiles) {
        message.setVisibility(GONE);
        View view = null;
        if(uploadAndDownloadViewHandler.isExist(currentMessage.getMessageId())) {
            view = uploadAndDownloadViewHandler.getUploadView(currentMessage.getMessageId()).getView();
        } else {
            view =layoutInflater.inflate(R.layout.audio,null);
            ImageView pp = view.findViewById(R.id.playpause);
            ImageView imageup = view.findViewById(R.id.imageup);
            RelativeLayout progressview = view.findViewById(R.id.progressview);
            CircularProgressBar eventButtton = view.findViewById(R.id.progressbutton);
            final String[] url = {sharedFiles.get(0).getUrl()};
            TextView durationview = view.findViewById(R.id.duration);
            SeekBar progressBar = view.findViewById(R.id.progressbar);
            final Boolean[] isPlaying = {false};
            pp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPlaying[0]) {
                        pp.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        stopAudio();
                        isPlaying[0] = false;
                    }else {
                        if(new File(envPath+downloadPath+"/"+sharedFiles.get(0).getName()+"."+sharedFiles.get(0).getExtension()).exists()) {
                            url[0] = envPath+downloadPath+"/"+sharedFiles.get(0).getName()+"."+sharedFiles.get(0).getExtension();
                        }
                        pp.setImageResource(R.drawable.ic_baseline_pause_24);
                        playeAudio(url[0],progressBar,pp,durationview);
                        isPlaying[0] = true;
                    }
                }
            });
            if(currentMessage.getMessageStatus() == MessageStatus.SENDING) {
                imageup.setImageResource(R.drawable.upload);
                imageup.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chatLayoutListener.onUploadRetry(currentMessage);
                    }
                });
            } else {
                Boolean canDownload = canShowDownloadButton(downloadPath,sharedFiles);
                if(canDownload) {
                    imageup.setVisibility(VISIBLE);
                    if(currentMessage.getMessageConfiguration().getMediaAutoDownload()) {
                        downloadFiles(imageup,progressview,sharedFiles,view,eventButtton);
                    } else {
                        View finalView = view;
                        imageup.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFiles(imageup,progressview,sharedFiles,finalView,eventButtton);
                            }
                        });
                    }
                } else {
                    imageup.setVisibility(GONE);
                }

            }

        }
        return view;
    }

    private View getRecordingLayout(ArrayList<SharedFile> sharedFiles) {
        message.setVisibility(GONE);
        View view = null;
        if(uploadAndDownloadViewHandler.isExist(currentMessage.getMessageId())) {
            view = uploadAndDownloadViewHandler.getUploadView(currentMessage.getMessageId()).getView();
        } else {
            view =layoutInflater.inflate(R.layout.recording,null);
            ImageView pp = view.findViewById(R.id.playpause);
            CircleImageView civ = view.findViewById(R.id.profilepicture);
            if(user.getProfileUrl().length() > 0) {
                Glide.with(getContext()).load(user.getProfileUrl()).override(100,100).into(civ);
            }
            ImageView imageup = view.findViewById(R.id.imageup);
            TextView durationview = view.findViewById(R.id.duration);
            final String[] url = {sharedFiles.get(0).getUrl()};
            SeekBar progressBar = view.findViewById(R.id.progressbar);
            final Boolean[] isPlaying = {false};
            pp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPlaying[0]) {
                        pp.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        stopAudio();
                        isPlaying[0] = false;
                    }else {
                        if(new File(envPath+downloadPath+"/"+sharedFiles.get(0).getName()+"."+sharedFiles.get(0).getExtension()).exists()) {
                            url[0] = envPath+downloadPath+"/"+sharedFiles.get(0).getName()+"."+sharedFiles.get(0).getExtension();
                        }
                        pp.setImageResource(R.drawable.ic_baseline_pause_24);
                        playeAudio(url[0],progressBar,pp,durationview);
                        isPlaying[0] = true;
                    }
                }
            });
            if(currentMessage.getMessageStatus() == MessageStatus.SENDING) {
                imageup.setImageResource(R.drawable.upload);
                imageup.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chatLayoutListener.onUploadRetry(currentMessage);
                    }
                });
            }
        }
        return view;
    }

    public void setUser(User user) {
        if(user != null) {
            this.user = user;
            this.senderName.setText(user.getName());
            this.senderName.setTextColor(user.getColor());
        } else {
            senderName.setVisibility(GONE);
        }
    }

    private String getFormatedDate(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String currentTime = sdf.format(date);
        return currentTime.toUpperCase();
    }

    private void updateMessageStatus(Message message) {
        if(message.getMessageStatus() == MessageStatus.SEEN) {
            this.messagestatus.setImageResource(R.drawable.seen);
        } else if(message.getMessageStatus() == MessageStatus.RECEIVED) {
            this.messagestatus.setImageResource(R.drawable.received);
        }  else if(message.getMessageStatus() == MessageStatus.SENT) {
            this.messagestatus.setImageResource(R.drawable.sent);
        } else if(message.getMessageStatus() == MessageStatus.SENDING) {
            this.messagestatus.setImageResource(R.drawable.waiting); //TODO Change icon
        }
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public DownloadHelper getDownloadHelper() {
        return downloadHelper;
    }

    public void setDownloadHelper(DownloadHelper downloadHelper) {
        this.downloadHelper = downloadHelper;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    private void playMusic(String url) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getContext(), Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
    }

    private void playeAudio(String url, SeekBar progressBar, ImageView pp, TextView durationview) {
        this.progressBar = progressBar;
        if(lastPlayingAudioMessageId == null || lastPlayingAudioMessageId.length() ==0) {
            lastPlayingAudioMessageId = currentMessage.getMessageId();
            lastPlayingProgressBar = progressBar;
            lastPlayingImageView = pp;
            this.duration = durationview;
            lastPlayingDuration = durationview;
            playMusic(url);
        } else {
            if(lastPlayingAudioMessageId == currentMessage.getMessageId()) {
                mediaPlayer.start();
            } else {
                stopAudio();
                mediaPlayer.reset();
                lastPlayingImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                lastPlayingProgressBar.setProgress(0);
                lastPlayingDuration.setText("00:00 / 00:00");
                lastPlayingAudioMessageId = currentMessage.getMessageId();
                lastPlayingProgressBar = progressBar;
                lastPlayingImageView = pp;
                this.duration = durationview;
                lastPlayingDuration = durationview;
                playMusic(url);
            }
        }

    }

    private void stopAudio() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();

        }
    }
}
