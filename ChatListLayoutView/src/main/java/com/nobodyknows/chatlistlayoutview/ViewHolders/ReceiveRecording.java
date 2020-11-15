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

public class ReceiveRecording extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    private String DOT_SEPRATOR = " \u25CF ";
    public ReceiveRecording(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message, User user) {
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+"Recording"+DOT_SEPRATOR+LayoutService.getSize(message.getSharedFiles().get(0).getSize())+DOT_SEPRATOR+LayoutService.getDuration(message.getSharedFiles().get(0).getDuration()));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        RoundedImageView profile = view.findViewById(R.id.profile);
        if(user.getProfileUrl() != null && user.getProfileUrl().length() > 0) {
            Glide.with(context).load(user.getProfileUrl()).into(profile);
        } else {
            Glide.with(context).load(R.drawable.ic_baseline_person_24).into(profile);
        }

        ImageView imageView = view.findViewById(R.id.playpause);
        SeekBar seekBar = view.findViewById(R.id.progressbar);
        LayoutService.initAudioPlayerView(context,message,imageView,seekBar);
    }
}
