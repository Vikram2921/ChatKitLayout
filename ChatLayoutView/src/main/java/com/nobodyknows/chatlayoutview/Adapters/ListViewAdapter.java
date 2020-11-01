package com.nobodyknows.chatlayoutview.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.nobodyknows.chatlayoutview.Model.User;
import com.nobodyknows.chatlayoutview.R;
import com.nobodyknows.chatlayoutview.ChatMessageView;
import com.nobodyknows.chatlayoutview.Model.Message;

import java.util.ArrayList;
import java.util.Map;

public class ListViewAdapter extends ArrayAdapter {
    private ArrayList<Message> messages;
    private Context context;
    private LayoutInflater layoutInflater;
    private Map<String, User> userMap;
    public ListViewAdapter(@NonNull Context context, int resource,ArrayList<Message> messages, Map<String, User> userMap) {
        super(context, resource,messages);
        this.context = context;
        this.userMap = userMap;
        this.messages = messages;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View view, ViewGroup parent) {
       View messageView = layoutInflater.inflate(R.layout.message_box,null,true);
       ChatMessageView chatMessageView = messageView.findViewById(R.id.messageview);
       chatMessageView.setMessage(messages.get(position));
       chatMessageView.setUser(userMap.get(messages.get(position).getSender()));
       return messageView;
    }
}
