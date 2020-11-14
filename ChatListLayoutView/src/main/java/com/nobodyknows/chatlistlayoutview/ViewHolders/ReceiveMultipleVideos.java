package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.SharedFile;
import com.nobodyknows.commonhelper.Model.User;

public class ReceiveMultipleVideos extends RecyclerView.ViewHolder {
    View view;
    private String DOT_SEPRATOR = " \u25CF ";
    public ReceiveMultipleVideos(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message,User user) {
        RoundedImageView image1 = view.findViewById(R.id.image1);
        RoundedImageView image2 = view.findViewById(R.id.image2);
        RoundedImageView image3 = view.findViewById(R.id.image3);
        RoundedImageView image4 = view.findViewById(R.id.image4);
        TextView duration1 = view.findViewById(R.id.duration1);
        TextView duration2 = view.findViewById(R.id.duration2);
        TextView duration3 = view.findViewById(R.id.duration3);
        TextView duration4 = view.findViewById(R.id.duration4);
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
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+message.getSharedFiles().size()+" Videos"+DOT_SEPRATOR+LayoutService.getSize(size));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        Glide.with(context).load(message.getSharedFiles().get(0).getPreviewUrl()).into(image1);
        Glide.with(context).load(message.getSharedFiles().get(1).getPreviewUrl()).into(image2);
        Glide.with(context).load(message.getSharedFiles().get(2).getPreviewUrl()).into(image3);
        Glide.with(context).load(message.getSharedFiles().get(3).getPreviewUrl()).into(image4);
        duration1.setText(LayoutService.getDuration(message.getSharedFiles().get(0).getDuration()));
        duration2.setText(LayoutService.getDuration(message.getSharedFiles().get(1).getDuration()));
        duration3.setText(LayoutService.getDuration(message.getSharedFiles().get(2).getDuration()));
        duration4.setText(LayoutService.getDuration(message.getSharedFiles().get(3).getDuration()));
    }
}
