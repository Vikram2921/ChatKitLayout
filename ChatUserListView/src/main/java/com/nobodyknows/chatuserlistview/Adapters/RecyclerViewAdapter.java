package com.nobodyknows.chatuserlistview.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.nobodyknows.chatuserlistview.MessageStatus;
import com.nobodyknows.chatuserlistview.Model.User;
import com.nobodyknows.chatuserlistview.R;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nobodyknows.chatuserlistview.ChatUserListView.myId;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> users;
    private Context context;
    public RecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.user_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        if(user.getName() != null && user.getName().length() > 0) {
            holder.name.setText(user.getName());
        } else {
            holder.name.setText(user.getUserId());
        }
        if(user.getProfileUrl() != null && user.getProfileUrl().length() > 0 ){
            Glide.with(context).load(user.getProfileUrl()).placeholder(R.drawable.ic_baseline_person_24).into(holder.profile);
        } else {
            Glide.with(context).load(R.drawable.ic_baseline_person_24).into(holder.profile);
        }
        holder.lasteMessage.setText(user.getLastMessage());
        holder.lastDate.setReferenceTime(user.getLastMessageDate().getTime());
        if(user.getLastMessageSender().equalsIgnoreCase(myId)) {
            if(user.getLastMessageStatus() == MessageStatus.SENT) {
                holder.status.setImageResource(R.drawable.sent);
            } else if(user.getLastMessageStatus() == MessageStatus.RECEIVED) {
                holder.status.setImageResource(R.drawable.received);
            } else if(user.getLastMessageStatus() == MessageStatus.SEEN) {
                holder.status.setImageResource(R.drawable.seen);
            } else if(user.getLastMessageStatus() == MessageStatus.SENDING) {
                holder.status.setImageResource(R.drawable.waiting);
            }
        } else {
            holder.status.setVisibility(View.GONE);
        }
        if(user.getUnreadMessageCount() > 0) {
            holder.lastDate.setTextColor(Color.parseColor("#4CAF50"));
             holder.unreadMessageCount.setVisibility(View.VISIBLE);
             holder.unreadMessageCount.setText(user.getUnreadMessageCount()+"");
        } else {
            holder.lastDate.setTextColor(Color.parseColor("#757575"));
             holder.unreadMessageCount.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profile;
        public TextView name;
        public TextView lasteMessage;
        public TextView unreadMessageCount;
        public ImageView status;
        public RelativeTimeTextView lastDate;
        public ViewHolder(View itemView) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.circleImageView);
            this.name = itemView.findViewById(R.id.name);
            this.lasteMessage = itemView.findViewById(R.id.lastemessage);
            this.lastDate = itemView.findViewById(R.id.lastdate);
            this.status = itemView.findViewById(R.id.status);
            this.unreadMessageCount = itemView.findViewById(R.id.unreadmessagecount);
        }
    }
}
