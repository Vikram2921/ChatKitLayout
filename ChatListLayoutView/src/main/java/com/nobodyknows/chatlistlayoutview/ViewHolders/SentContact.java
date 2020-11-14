package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.chatlistlayoutview.ViewContacts;
import com.nobodyknows.commonhelper.Model.Message;

public class SentContact extends RecyclerView.ViewHolder {
    View view;
    private String DOT_SEPRATOR = " \u25CF ";
    public SentContact(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message) {
        TextView messageTime = view.findViewById(R.id.messagetime);
        ImageView messageStatus = view.findViewById(R.id.messagestatus);
        LayoutService.updateMessageStatus(message.getMessageStatus(),messageStatus);
        TextView textView = view.findViewById(R.id.name);
        TextView viewContact = view.findViewById(R.id.viewcontact);
        if(message.getContacts().size() == 1) {
            textView.setText(message.getContacts().get(0).getName());
            messageTime.setText("1 Contact"+DOT_SEPRATOR+LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        } else {
            textView.setText(message.getContacts().get(0).getName()+"... +"+(message.getContacts().size()-1)+" Contacts");
            messageTime.setText(message.getContacts().size()+" Contacts"+DOT_SEPRATOR+LayoutService.getFormatedDate("hh:mm aa", message.getSentAt()));
        }
        viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewContacts.class);
                intent.putParcelableArrayListExtra("contacts",LayoutService.getParceableList(message.getContacts()));
                context.startActivity(intent);
            }
        });
    }

}
