package com.nobodyknows.chatlayoutview.ViewHolders;

import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlayoutview.R;
import com.nobodyknows.chatlayoutview.LayoutService;
import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;
import com.nobodyknows.commonhelper.Model.Message;

public class TextMessageViewRight extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    public TextMessageViewRight(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Message message,ChatLinkDatabaseHelper chatLinkDatabaseHelper) {
        TextView messageText = view.findViewById(R.id.message);
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageText.setAutoLinkMask(Linkify.ALL);
        messageText.setText(message.getMessage());
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        ImageView messageStatus = view.findViewById(R.id.messagestatus);
        LayoutService.updateMessageStatus(message.getMessageStatus(),messageStatus);
    }
}
