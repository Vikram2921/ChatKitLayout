package com.nobodyknows.chatmessageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nobodyknows.chatmessageview.CONSTANT.MessagePosition;
import com.nobodyknows.chatmessageview.Model.Message;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ChatMessageView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private RelativeLayout root,rootview,messageBox;
    private TextView message,messageTime;
    private ImageView messagestatus;
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
        message = root.findViewById(R.id.message);
        messageBox = root.findViewById(R.id.messagebox);
        messagestatus = root.findViewById(R.id.messagestatus);
        messageTime = root.findViewById(R.id.messagetime);
    }

    private void configRootView(MessagePosition position) {
        RelativeLayout.LayoutParams params = (LayoutParams) rootview.getLayoutParams();
        if(position == MessagePosition.LEFT) {
            params.setMargins(0,0,100,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            messageBox.setBackgroundResource(R.drawable.left_message_drawable);
        } else if(position == MessagePosition.RIGHT) {
            params.setMargins(100,0,0,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            messageBox.setBackgroundResource(R.drawable.right_message_drawable);
        }
        rootview.setLayoutParams(params);
    }


    public void setMessage(Message message) {
        configRootView(message.getMessageConfiguration().getMessagePosition());
        this.message.setText(message.getMessage());
        this.messageTime.setText(getFormatedDate("hh:mm aa",message.getSentAt()));
        updateMessageStatus(message);
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
