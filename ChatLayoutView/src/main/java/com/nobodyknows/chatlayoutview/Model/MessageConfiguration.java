package com.nobodyknows.chatlayoutview.Model;

import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;

import static com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition.LEFT;

public class MessageConfiguration {
    private MessagePosition messagePosition = LEFT;

    public MessagePosition getMessagePosition() {
        return messagePosition;
    }

    public void setMessagePosition(MessagePosition messagePosition) {
        this.messagePosition = messagePosition;
    }
}
