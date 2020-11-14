package com.nobodyknows.commonhelper.Services;

import android.view.View;
import android.view.ViewGroup;

import com.nobodyknows.circularprogressbutton.ProgressButton;
import com.nobodyknows.commonhelper.Model.Message;


public class UploadAndDownloadView {

    private ProgressButton progressButton;

    private Message message;

    public ProgressButton getProgressButton() {
        ViewGroup parentGroup = ((ViewGroup) progressButton.getParent());
        if(parentGroup != null) {
            parentGroup.removeView(progressButton);
        }
        return progressButton;
    }

    public void setProgressButton(ProgressButton progressButton) {
        this.progressButton = progressButton;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
