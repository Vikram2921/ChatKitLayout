package com.nobodyknows.chatlayoutview.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlayoutview.R;
import com.nobodyknows.chatmessageview.ChatMessageView;
import com.nobodyknows.chatmessageview.Model.Message;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> messages;
    private Context context;
    public RecyclerViewAdapter(Context context,ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
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
