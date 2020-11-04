package com.nobodyknows.chatlayoutview.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.DownloadHelper;
import com.nobodyknows.chatlayoutview.Model.User;
import com.nobodyknows.chatlayoutview.R;
import com.nobodyknows.chatlayoutview.ChatMessageView;
import com.nobodyknows.chatlayoutview.Model.Message;

import java.util.ArrayList;
import java.util.Map;

import static com.nobodyknows.chatlayoutview.ChatLayoutView.downloadHelper;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Message> messages;
    private Context context;
    private Map<String,User> userMap;
    private Map<MessageType,String> downloadPath;
    private MediaPlayer mediaPlayer;
    public RecyclerViewAdapter(Context context, ArrayList<Message> messages, Map<String, User> userMap, Map<MessageType,String> downloadPaths,MediaPlayer mediaPlayer) {
        this.context = context;
        this.messages = messages;
        this.mediaPlayer = mediaPlayer;
        this.userMap = userMap;
        this.downloadPath = downloadPaths;
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
        holder.chatMessageView.setDownloadPath(getUrl(message.getMessageType()));
        holder.chatMessageView.setUser(userMap.get(message.getSender()));
        holder.chatMessageView.setDownloadHelper(downloadHelper);
        holder.chatMessageView.setMediaPlayer(mediaPlayer);
        holder.chatMessageView.setMessage(message);
    }

    private String getUrl(MessageType messageType) {
        return downloadPath.get(messageType);
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
