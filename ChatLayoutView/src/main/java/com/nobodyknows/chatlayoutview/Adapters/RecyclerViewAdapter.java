package com.nobodyknows.chatlayoutview.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlayoutview.Model.User;
import com.nobodyknows.chatlayoutview.R;
import com.nobodyknows.chatlayoutview.ChatMessageView;
import com.nobodyknows.chatlayoutview.Model.Message;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> messages;
    private Context context;
    private Map<String,User> userMap;
    public RecyclerViewAdapter(Context context, ArrayList<Message> messages, Map<String, User> userMap) {
        this.context = context;
        this.messages = messages;
        this.userMap = userMap;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.message_box,parent,false);
        ViewHolder viewHolder = new ViewHolder(item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.setIsRecyclable(false);
        holder.chatMessageView.setMessage(message);
        holder.chatMessageView.setUser(userMap.get(message.getSender()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ChatMessageView chatMessageView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.chatMessageView = itemView.findViewById(R.id.messageview);
        }
    }
}
