package com.nobodyknows.chatlayoutview.Interfaces;

import android.view.View;

import com.nobodyknows.chatlayoutview.Model.Message;

public interface UpDownCompleteListener {
    public void onUploadComplete(Message message, View view);
    public void onDownloadComplete(Message message, View view);
}
