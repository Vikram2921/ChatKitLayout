package com.nobodyknows.chatmessageview.Model;

import com.nobodyknows.chatmessageview.CONSTANT.MessagePosition;

import static com.nobodyknows.chatmessageview.CONSTANT.MessagePosition.LEFT;

public class MessageConfiguration {
    private MessagePosition messagePosition = LEFT;

    public MessagePosition getMessagePosition() {
        return messagePosition;
    }

    public void setMessagePosition(MessagePosition messagePosition) {
        this.messagePosition = messagePosition;
    }
}
