package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.circularprogressbutton.ProgressButton;
import com.nobodyknows.circularprogressbutton.ProgressClickListener;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.User;

public class ReceiveSingleImage extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    private String DOT_SEPRATOR = " \u25CF ";
    public ReceiveSingleImage(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message, User user) {
        RoundedImageView roundedImageView = view.findViewById(R.id.image);
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+"Image"+DOT_SEPRATOR+LayoutService.getSize(message.getSharedFiles().get(0).getSize()));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        Glide.with(context).load(message.getSharedFiles().get(0).getPreviewUrl()).into(roundedImageView);
        LayoutService.handlerDownloadAndUploadCase(context,view,message);
        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutService.changeToGalleryIntent(context,message);
            }
        });
    }
}
