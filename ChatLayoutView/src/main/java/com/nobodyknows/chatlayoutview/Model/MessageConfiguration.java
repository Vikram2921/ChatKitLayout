package com.nobodyknows.chatlayoutview.Model;

import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;
import com.nobodyknows.chatlayoutview.R;

import static com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition.LEFT;

public class MessageConfiguration {
    private MessagePosition messagePosition = LEFT;
    private int backgroundResource = R.drawable.left_message_drawable;
    private float messageTextSize =16;
    public MessagePosition getMessagePosition() {
        return messagePosition;
    }

    public void setMessagePosition(MessagePosition messagePosition) {
        this.messagePosition = messagePosition;
    }

    public int getBackgroundResource() {
        return backgroundResource;
    }

    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    public float getMessageTextSize() {
        return messageTextSize;
    }

    public void setMessageTextSize(float messageTextSize) {
        this.messageTextSize = messageTextSize;
    }
}
