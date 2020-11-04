package com.nobodyknows.chatlayoutview.Services;

import android.view.View;
import android.view.ViewGroup;

import com.nobodyknows.chatlayoutview.Interfaces.UpDownCompleteListener;
import com.nobodyknows.chatlayoutview.Model.Message;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class UploadAndDownloadView {

    private View view;

    private Message message;

    public View getView() {
        ViewGroup parentGroup = ((ViewGroup) view.getParent());
        if(parentGroup != null) {
            parentGroup.removeView(view);
        }
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
