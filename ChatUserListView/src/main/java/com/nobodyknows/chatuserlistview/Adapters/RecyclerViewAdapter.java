package com.nobodyknows.chatuserlistview.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.nobodyknows.chatuserlistview.Listeners.ChatUserListViewListener;
import com.nobodyknows.chatuserlistview.Model.User;
import com.nobodyknows.chatuserlistview.R;
import com.nobodyknows.chatuserlistview.view_profile;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.vistrav.pop.Pop;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nobodyknows.chatuserlistview.ChatUserListView.myId;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> users;
    private Context context;
    private AlertDialog pop;
    private Activity activity;
    private ChatUserListViewListener chatUserListViewListener;
    public RecyclerViewAdapter(Context context, ArrayList<User> users, ChatUserListViewListener chatUserListViewListener, Activity activity) {
        this.context = context;
        this.users = users;
        this.chatUserListViewListener = chatUserListViewListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.user_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(item);
        return viewHolder;
    }

    private void updateDrawable(TextView textView, MessageType messageType, String messgae, String senderId, String name) {
        String message = "You shared ";
        if(!senderId.equalsIgnoreCase(myId)) {
            message = name + " has shared ";
        }
        if(messageType == MessageType.IMAGE) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_image_24,0,0,0);
            message+="images";
        } else if(messageType == MessageType.VIDEO) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_videocam_14,0,0,0);
            message+="videos";
        } else if(messageType == MessageType.AUDIO) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_headset_14,0,0,0);
            message+="audios";
        }  else if(messageType == MessageType.GIF) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_gif_24,0,0,0);
            message="";
        }  else if(messageType == MessageType.RECORDING) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_mic_24,0,0,0);
            message+="an audio message";
        }  else if(messageType == MessageType.DOCUMENT) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_insert_drive_file_24,0,0,0);
            message+="documents";
        }  else if(messageType == MessageType.CONTACT) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_contacts_24,0,0,0);
            message+="contacts";
        }  else if(messageType == MessageType.STICKER) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_emoji_emotions_24,0,0,0);
            message+="sticker";
        }   else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            message = messgae;
        }
        textView.setText(message);
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
        updateDrawable(holder.lasteMessage,user.getLastMessageType(),user.getLastMessage(),user.getUserId(),user.getName());
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatUserListViewListener.onUserSelect(user);
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pop = Pop.on(activity).with()
                        .cancelable(true)
                        .layout(R.layout.profile_view)
                        .show(new Pop.View() {
                            @Override
                            public void prepare(@Nullable View view) {
                                ImageView profile = view.findViewById(R.id.profile);
                                if (user.getProfileUrl() != null && user.getProfileUrl().length() > 0) {
                                    Glide.with(context).load(user.getProfileUrl()).placeholder(R.drawable.ic_baseline_person_24).into(profile);
                                } else {
                                    Glide.with(context).load(R.drawable.ic_baseline_person_24).into(profile);
                                }
                                profile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, view_profile.class);
                                        intent.putExtra("name",holder.name.getText());
                                        intent.putExtra("profile",user.getProfileUrl());
                                        context.startActivity(intent);
                                        pop.cancel();
                                    }
                                });
                                TextView name = view.findViewById(R.id.name);
                                name.setText(holder.name.getText());
                                ImageView chat = view.findViewById(R.id.chat);
                                ImageView callaudio = view.findViewById(R.id.callaudio);
                                ImageView callvideo = view.findViewById(R.id.callvideo);
                                ImageView info = view.findViewById(R.id.info);
                                chat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chatUserListViewListener.onUserSelect(user);
                                        pop.cancel();
                                    }
                                });

                                callaudio.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chatUserListViewListener.onClickAudioCall(user);
                                        pop.cancel();
                                    }
                                });

                                callvideo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chatUserListViewListener.onClickVideoCall(user);
                                        pop.cancel();
                                    }
                                });

                                info.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chatUserListViewListener.onClickInfoButton(user);
                                        pop.cancel();
                                    }
                                });

                            }
                        });

            }
        });
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
