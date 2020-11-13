package com.nobodyknows.commonhelper.Interfaces;

import android.view.View;

import com.nobodyknows.commonhelper.Model.Message;

public interface ChatLayoutListener {
    public void onSwipeToReply(Message message, View replyView);
    public void onUploadRetry(Message message);
}
