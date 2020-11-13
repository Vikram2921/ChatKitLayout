package com.nobodyknows.commonhelper.Model;

import android.graphics.Color;

import com.nobodyknows.commonhelper.CONSTANT.MessagePosition;
import com.nobodyknows.commonhelper.R;

import static com.nobodyknows.commonhelper.CONSTANT.MessagePosition.LEFT;

public class MessageConfiguration {
    private MessagePosition messagePosition = LEFT;
    private int backgroundResource;
    private float messageTextSize =16;
    private Boolean mediaAutoDownload =false;
    private int textColor = Color.BLACK;
    private int timeTextColor = Color.parseColor("#969696");
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

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTimeTextColor() {
        return timeTextColor;
    }

    public void setTimeTextColor(int timeTextColor) {
        this.timeTextColor = timeTextColor;
    }

    public Boolean getMediaAutoDownload() {
        return mediaAutoDownload;
    }

    public void setMediaAutoDownload(Boolean mediaAutoDownload) {
        this.mediaAutoDownload = mediaAutoDownload;
    }
}
