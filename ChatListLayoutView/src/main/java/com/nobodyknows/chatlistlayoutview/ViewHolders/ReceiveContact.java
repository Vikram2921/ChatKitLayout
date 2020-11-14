package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.chatlistlayoutview.ViewContacts;
import com.nobodyknows.circularprogressbutton.ProgressButton;
import com.nobodyknows.circularprogressbutton.ProgressClickListener;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.User;

public class ReceiveContact extends RecyclerView.ViewHolder {
    View view;
    private String DOT_SEPRATOR = " \u25CF ";
    public ReceiveContact(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(Context context,Message message, User user) {
        TextView messageTime = view.findViewById(R.id.messagetime);
        TextView sender = view.findViewById(R.id.sendername);
        sender.setText(user.getName());
        sender.setTextColor(user.getColor());
        TextView textView = view.findViewById(R.id.name);
        TextView viewContact = view.findViewById(R.id.viewcontact);
        if(message.getContacts().size() == 1) {
            textView.setText(message.getContacts().get(0).getName());
            messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+"1 Contact");
        } else {
            textView.setText(message.getContacts().get(0).getName()+"... +"+(message.getContacts().size()-1)+" Contacts");
            messageTime.setText(LayoutService.getFormatedDate("hh:mm aa", message.getSentAt())+DOT_SEPRATOR+message.getContacts().size()+" Contacts");
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
