package com.nobodyknows.commonhelper.Services;

import android.content.Context;
import android.view.View;

import com.nobodyknows.commonhelper.Model.Message;

import java.util.ArrayList;

public class UploadAndDownloadViewHandler {
    private Context context;
    private ArrayList<String> messageIds = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    public UploadAndDownloadViewHandler(Context context) {
        this.context = context;
    }

    public void addUploadView(Message message, View view) {
        if(!messageIds.contains(message.getMessageId())) {
            this.messageIds.add(message.getMessageId());
            this.views.add(view);
            this.messages.add(message);
        }
    }

    public boolean isExist(String messageId) {
        return messageIds.contains(messageId);
    }

    public UploadAndDownloadView getUploadView(String messageId) {
        UploadAndDownloadView getChatView = new UploadAndDownloadView();
        int index = messageIds.indexOf(messageId);
        if(index > -1) {
            getChatView.setMessage(messages.get(index));
            getChatView.setView(views.get(index));
        }
        return getChatView;
    }

    public void clear() {
        this.messageIds.clear();
        this.views.clear();
        this.messages.clear();
    }

    public void delete(String messageId) {
        int index = messageIds.indexOf(messageId);
        if(index > -1) {
            this.views.remove(index);
            this.messages.remove(index);
            this.messageIds.remove(index);
        }
    }
}
