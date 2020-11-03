package com.nobodyknows.chatlayoutview.Interfaces;

import android.view.View;

import com.nobodyknows.chatlayoutview.Model.Message;

public interface ChatLayoutListener {
    public void onSwipeToReply(Message message, View replyView);
}
