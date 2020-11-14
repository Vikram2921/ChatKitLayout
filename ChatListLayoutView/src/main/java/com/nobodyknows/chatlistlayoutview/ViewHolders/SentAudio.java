package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
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

import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.uploadAndDownloadViewHandler;

public class SentAudio extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    private String DOT_SEPRATOR = " \u25CF ";
    public SentAudio(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message) {
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageTime.setText("Audio"+DOT_SEPRATOR+LayoutService.getSize(message.getSharedFiles().get(0).getSize())+DOT_SEPRATOR+LayoutService.getDuration(message.getSharedFiles().get(0).getDuration())+DOT_SEPRATOR+LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        ImageView messageStatus = view.findViewById(R.id.messagestatus);
        LayoutService.updateMessageStatus(message.getMessageStatus(),messageStatus);
        ProgressButton progressButton = view.findViewById(R.id.probutton);
        progressButton.initalize();
        progressButton.setUploadType();
        progressButton.setProgressClickListener(new ProgressClickListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
