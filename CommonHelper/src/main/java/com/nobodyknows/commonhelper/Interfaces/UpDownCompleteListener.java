package com.nobodyknows.commonhelper.Interfaces;

import android.view.View;

import com.nobodyknows.commonhelper.Model.Message;

public interface UpDownCompleteListener {
    public void onUploadComplete(Message message, View view);
    public void onDownloadComplete(Message message, View view);
}
