package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
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

import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.downloadPaths;

public class ReceiveAudio extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    private String DOT_SEPRATOR = " \u25CF ";
    public ReceiveAudio(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message, User user) {
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+"Audio"+DOT_SEPRATOR+LayoutService.getSize(message.getSharedFiles().get(0).getSize())+DOT_SEPRATOR+LayoutService.getDuration(message.getSharedFiles().get(0).getDuration()));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        ProgressButton progressButton = view.findViewById(R.id.probutton);
        progressButton.initalize();
        progressButton.setProgressClickListener(new ProgressClickListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCancel() {

            }
        });

        ImageView imageView = view.findViewById(R.id.playpause);
        SeekBar seekBar = view.findViewById(R.id.progressbar);
        LayoutService.initAudioPlayerView(context,message,imageView,seekBar);
    }
}
