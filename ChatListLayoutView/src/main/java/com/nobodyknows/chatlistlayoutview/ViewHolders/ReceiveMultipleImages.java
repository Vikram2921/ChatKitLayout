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
import com.nobodyknows.commonhelper.Model.SharedFile;
import com.nobodyknows.commonhelper.Model.User;

public class ReceiveMultipleImages extends RecyclerView.ViewHolder {
    View view;
    private String DOT_SEPRATOR = " \u25CF ";
    public ReceiveMultipleImages(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message,User user) {
        RoundedImageView image1 = view.findViewById(R.id.image1);
        RoundedImageView image2 = view.findViewById(R.id.image2);
        RoundedImageView image3 = view.findViewById(R.id.image3);
        RoundedImageView image4 = view.findViewById(R.id.image4);
        TextView count = view.findViewById(R.id.numbercount);
        if(message.getSharedFiles().size() > 4) {
            count.setVisibility(View.VISIBLE);
            count.setText("+"+(message.getSharedFiles().size()-4));
        }
        TextView messageTime = view.findViewById(R.id.messagetime);
        double size = 0.0;
        for(SharedFile sharedFile :message.getSharedFiles()) {
            size += sharedFile.getSize();
        }
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+message.getSharedFiles().size()+" Images"+DOT_SEPRATOR+LayoutService.getSize(size));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        Glide.with(context).load(message.getSharedFiles().get(0).getPreviewUrl()).into(image1);
        Glide.with(context).load(message.getSharedFiles().get(1).getPreviewUrl()).into(image2);
        Glide.with(context).load(message.getSharedFiles().get(2).getPreviewUrl()).into(image3);
        Glide.with(context).load(message.getSharedFiles().get(3).getPreviewUrl()).into(image4);
        LayoutService.handlerDownloadAndUploadCase(context,view,message);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutService.changeToGalleryIntent(context,message,0);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutService.changeToGalleryIntent(context,message,1);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutService.changeToGalleryIntent(context,message,2);
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutService.changeToGalleryIntent(context,message,3);
            }
        });
    }
}
