package com.nobodyknows.commonhelper.Services;

import android.content.Context;

import com.nobodyknows.circularprogressbutton.ProgressButton;
import com.nobodyknows.commonhelper.Model.Message;

import java.util.ArrayList;

public class UploadAndDownloadViewHandler {
    private Context context;
    private ArrayList<String> messageIds = new ArrayList<>();
    private ArrayList<ProgressButton> progressBars = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    public UploadAndDownloadViewHandler(Context context) {
        this.context = context;
    }

    public void addProgressButtons(Message message, ProgressButton progressButton) {
        if(!messageIds.contains(message.getMessageId())) {
            this.messageIds.add(message.getMessageId());
            this.progressBars.add(progressButton);
            this.messages.add(message);
        }
    }

    public boolean isExist(String messageId) {
        return messageIds.contains(messageId);
    }

    public ProgressButtonContainer getProgressButtonContainer(String messageId) {
        ProgressButtonContainer getChatView = new ProgressButtonContainer();
        int index = messageIds.indexOf(messageId);
        if(index > -1) {
            getChatView.setMessage(messages.get(index));
            getChatView.setProgressButton(progressBars.get(index));
        }
        return getChatView;
    }

    public void clear() {
        this.messageIds.clear();
        this.progressBars.clear();
        this.messages.clear();
    }

    public void delete(String messageId) {
        int index = messageIds.indexOf(messageId);
        if(index > -1) {
            this.progressBars.remove(index);
            this.messages.remove(index);
            this.messageIds.remove(index);
        }
    }
}
