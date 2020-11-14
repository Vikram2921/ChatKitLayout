package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.circularprogressbutton.ProgressButton;
import com.nobodyknows.circularprogressbutton.ProgressClickListener;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.User;

import org.w3c.dom.Text;

import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.downloadPaths;

public class DocumentMessageViewLeft extends RecyclerView.ViewHolder {
    View view;
    private String DOT_SEPRATOR = " \u25CF ";
    TextView textView;
    public DocumentMessageViewLeft(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message, User user) {
        TextView messageTime = view.findViewById(R.id.messagetime);
        messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+"Document"+DOT_SEPRATOR+LayoutService.getSize(message.getSharedFiles().get(0).getSize()));
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());

        RoundedImageView preview = view.findViewById(R.id.preview_file);
        ImageView fileicon = view.findViewById(R.id.fileicon);
        TextView documentName = view.findViewById(R.id.documentname);
        TextView documentInfo = view.findViewById(R.id.documentinfo);
        int resId = context.getResources().getIdentifier(message.getSharedFiles().get(0).getExtension(),"drawable",context.getPackageName());
        if(resId != 0){
            fileicon.setImageResource(resId);
        }
        documentName.setText(message.getSharedFiles().get(0).getName());
        documentInfo.setText(message.getSharedFiles().get(0).getFileInfo());
        ProgressButton progressButton = view.findViewById(R.id.probutton);
        if(LayoutService.canShowDownloadButton(downloadPaths.get(MessageType.DOCUMENT),message.getSharedFiles())) {
            progressButton.initalize();
            progressButton.setProgressClickListener(new ProgressClickListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            progressButton.setVisibility(View.GONE);
        }

    }
}
