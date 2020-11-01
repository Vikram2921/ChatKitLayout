package com.nobodyknows.chatlayoutview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nobodyknows.chatlayoutview.Activities.viewmedia;
import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.SharedFile;
import com.nobodyknows.chatlayoutview.Model.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import xyz.neocrux.suziloader.SuziLoader;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

public class ChatMessageView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private RelativeLayout root,rootview,messageBox,dateview;
    private TextView message,messageTime,senderName;
    private ImageView messagestatus;
    private User user;
    private String downloadPath;
    private String envPath = Environment.getExternalStorageDirectory().getPath();
    private DownloadHelper downloadHelper;
    private LinearLayout customView;
    private Message currentMessage;
    private SuziLoader suziLoader = new SuziLoader();
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
            messageBox.setBackgroundResource(R.drawable.left_message_drawable);
        } else if(position == MessagePosition.RIGHT) {
            params.setMargins(100,0,0,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            messageBox.setBackgroundResource(R.drawable.right_message_drawable);
        }
        rootview.setLayoutParams(params);
    }


    public void setMessage(Message message) {
        this.currentMessage = message;
        if(message.getMessageType() == MessageType.DATE) {
            TextView textView = root.findViewById(R.id.date);
            textView.setText(message.getMessage());
            this.dateview.setVisibility(VISIBLE);
            this.rootview.setVisibility(GONE);
        } else {
            configRootView(message.getMessageConfiguration().getMessagePosition());
            this.message.setText(message.getMessage());
            this.messageTime.setText(getFormatedDate("hh:mm aa",message.getSentAt()));
            updateMessageStatus(message);
            if(message.getMessageType() != MessageType.TEXT) {
                updateMessageView(message);
            }
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
        if(currentMessage.getMessageType() == MessageType.VIDEO) {
            playIcon.setVisibility(VISIBLE);
            Glide.with(getContext()).load(sharedFile.getUrl()).into(imageView);
        } else {
            Glide.with(getContext()).load(sharedFile.getUrl()).into(imageView);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, height);
        layoutParams.weight =1;
        layoutParams.setMargins(3,3,3,3);
        view.setLayoutParams(layoutParams);
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

    private View getMediaLayout(ArrayList<SharedFile> sharedFiles,MessageType messageType) {
        View view = layoutInflater.inflate(R.layout.image_chat_view,null);
        LinearLayout line1 = view.findViewById(R.id.line1);
        LinearLayout line2 = view.findViewById(R.id.line2);
        CircularProgressButton eventButtton = view.findViewById(R.id.circularButton);
        Boolean canDownload = canShowDownloadButton(downloadPath,sharedFiles);
        if(canDownload) {
            eventButtton.setVisibility(VISIBLE);
        } else {
            eventButtton.setVisibility(GONE);
        }
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for(SharedFile sharedFile:sharedFiles) {
            urls.add(sharedFile.getUrl());
            names.add(sharedFile.getName()+"."+sharedFile.getExtension());
        }
        eventButtton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                eventButtton.startAnimation();
                if(sharedFiles.size() == 1) {
                    downloadHelper.downloadSingle(sharedFiles.get(0),downloadPath,eventButtton);
                } else {
                    downloadHelper.downloadAll(sharedFiles,downloadPath,eventButtton);
                }
            }
        });
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
        return view;
    }

    private ArrayList<String> getNames(Message message,int size) {
        ArrayList<String> list =new ArrayList<>();
        for(int i=0;i<size;i++) {
            list.add(message.getMessageId()+i+"_"+message.getMessageType().ordinal()+".jpg");
        }
        return list;
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
            default:
                break;
        }
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
        if(message.getSeenAt() != null) {
            this.messagestatus.setImageResource(R.drawable.seen);
        } else if(message.getReceivedAt() != null) {
            this.messagestatus.setImageResource(R.drawable.received);
        }  else if(message.getSentAt() != null) {
            this.messagestatus.setImageResource(R.drawable.sent);
        } else {

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
}
