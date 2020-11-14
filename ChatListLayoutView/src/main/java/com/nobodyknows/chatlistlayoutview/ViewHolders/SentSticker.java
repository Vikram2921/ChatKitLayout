package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.commonhelper.Model.Message;

public class SentSticker extends RecyclerView.ViewHolder {
    View view;
    private String DOT_SEPRATOR = " \u25CF ";
    public SentSticker(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message) {
        ImageView imageView = view.findViewById(R.id.image);
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        ImageView messageStatus = view.findViewById(R.id.messagestatus);
        LayoutService.updateMessageStatus(message.getMessageStatus(),messageStatus);
        Glide.with(context).load(message.getSharedFiles().get(0).getPreviewUrl()).into(imageView);
    }

}
