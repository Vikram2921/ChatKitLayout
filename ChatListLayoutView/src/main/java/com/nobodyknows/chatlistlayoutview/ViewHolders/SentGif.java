package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.os.Handler;
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



public class SentGif extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    private Handler handler;
    private String DOT_SEPRATOR = " \u25CF ";
    public SentGif(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message) {
        handler = new Handler();
        ImageView roundedImageView = view.findViewById(R.id.image);
        TextView messageTime = view.findViewById(R.id.messagetime);
        ImageView playService = view.findViewById(R.id.gifplay);
        messageTime.setText("Gif"+DOT_SEPRATOR+LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        Glide.with(context).asBitmap().load(message.getSharedFiles().get(0).getPreviewUrl()).into(roundedImageView);
        ImageView messageStatus = view.findViewById(R.id.messagestatus);
        LayoutService.updateMessageStatus(message.getMessageStatus(),messageStatus);
        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(context,roundedImageView,message.getSharedFiles().get(0).getUrl(),playService);
            }
        });
    }

    private void startTimer(Context context,ImageView roundedImageView,String url,ImageView play) {
        play.setVisibility(View.GONE);
        Glide.with(context).asGif().load(url).into(roundedImageView);
       Runnable runnable = new Runnable() {
           @Override
           public void run() {
               Glide.with(context).asBitmap().load(url).into(roundedImageView);
               play.setVisibility(View.VISIBLE);
           }
       };
       handler.postDelayed(runnable,15000);

    }
}
