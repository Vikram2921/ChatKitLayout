package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlinkpreview.ChatLinkView;
import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.User;

public class LinkTextMessageViewLeft extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    public LinkTextMessageViewLeft(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Message message, User user) {
        ChatLinkView chatLinkView = view.findViewById(R.id.linkview);
        TextView messageText = view.findViewById(R.id.message);
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageText.setAutoLinkMask(Linkify.ALL);
        messageText.setText(message.getMessage());
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        LayoutService.checkForLink(chatLinkView,messageText);
    }
}
