package com.nobodyknows.chatlayoutview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatMessageView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private RelativeLayout root,rootview,messageBox,dateview;
    private TextView message,messageTime,senderName;
    private ImageView messagestatus;
    private User user;
    private LinearLayout customView;
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

    private ImageView getImageView(String url,int height) {
        ImageView imageView = new ImageView(getContext());
        Glide.with(getContext()).load(url).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, height);
        layoutParams.weight =1;
        layoutParams.setMargins(3,3,3,3);
        imageView.setLayoutParams(layoutParams);
        return imageView;
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

    private View getImageLayout(ArrayList<String> urls) {
        View view = layoutInflater.inflate(R.layout.image_chat_view,null);
        LinearLayout line1 = view.findViewById(R.id.line1);
        LinearLayout line2 = view.findViewById(R.id.line2);
        int height = getHeightByUrlsSize(urls.size());
        for(int i=0;i<urls.size();i++) {
            if(i<2) {
                line1.addView(getImageView(urls.get(i),height));
            } else {
                line2.addView(getImageView(urls.get(i),height));
            }
        }
        return view;
    }

    private void updateMessageView(Message message) {
        customView.setVisibility(VISIBLE);
        switch (message.getMessageType())
        {
            case IMAGE:
                customView.addView(getImageLayout(message.getUrls()));
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
}
