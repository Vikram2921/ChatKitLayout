package com.nobodyknows.chatlayoutview.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.nobodyknows.chatlayoutview.R;
import com.nobodyknows.chatmessageview.ChatMessageView;
import com.nobodyknows.chatmessageview.Model.Message;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter {
    private ArrayList<Message> messages;
    private Context context;
    private LayoutInflater layoutInflater;
    public ListViewAdapter(@NonNull Context context, int resource,ArrayList<Message> messages) {
        super(context, resource,messages);
        this.context = context;
        this.messages = messages;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View view, ViewGroup parent) {
       View messageView = layoutInflater.inflate(R.layout.message_box,null,true);
       ChatMessageView chatMessageView = messageView.findViewById(R.id.messageview);
       chatMessageView.setMessage(messages.get(position));
       return messageView;
    }
}
