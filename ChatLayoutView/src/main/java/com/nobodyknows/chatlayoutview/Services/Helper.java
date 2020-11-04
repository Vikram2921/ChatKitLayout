package com.nobodyknows.chatlayoutview.Services;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.User;
import com.nobodyknows.chatlayoutview.R;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class Helper {
    private Context context;
    private LayoutInflater layoutInflater;
    private Map<String,User> userMap;
    private String myId;
    public Helper(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userMap = new HashMap<>();
    }

    public void setUserMap(Map<String,User> userMap) {
        this.userMap = userMap;
    }

    public Map<String, User> getUserMap() {
        return this.userMap;
    }

    public void addUser(User user) {
        this.userMap.put(user.getUserId(),user);
    }

    public User getUser(String userID) {
        return userMap.get(userID);
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getReplyMessageView(Message message) {
        View view = layoutInflater.inflate(R.layout.replyview,null);
        ImageView preview = view.findViewById(R.id.preview);
        TextView senderName = view.findViewById(R.id.sendername);
        TextView messageview = view.findViewById(R.id.message);
        View bar = view.findViewById(R.id.bar);
        if(message.getMessageType() == MessageType.DATE) {
            senderName.setText("Date");
            messageview.setText(message.getMessage());
            senderName.setTextColor(Color.BLACK);
            bar.setBackgroundColor(Color.BLACK);
            preview.setVisibility(GONE);
        } else {
            User user =getUser(message.getSender());
            senderName.setTextColor(user.getColor());
            if(message.getSender().equalsIgnoreCase(myId)) {
                senderName.setText("You");
            } else {
                senderName.setText(user.getName());
            }
            if(message.getMessageType() == MessageType.IMAGE) {
                messageview.setText("Photo");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_image_24,0,0,0);
                Glide.with(getContext()).load(message.getSharedFiles().get(0).getUrl()).override(100,100).into(preview);
            } else if(message.getMessageType() == MessageType.VIDEO) {
                messageview.setText("Video");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_image_24,0,0,0);
                Glide.with(getContext()).load(message.getSharedFiles().get(0).getUrl()).override(100,100).into(preview);
            } else if(message.getMessageType() == MessageType.AUDIO) {
                messageview.setText("Audio");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_image_24,0,0,0);
            }  else if(message.getMessageType() == MessageType.GIF) {
                messageview.setText("GIF");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_gif_24,0,0,0);
                Glide.with(getContext()).asBitmap().load(message.getSharedFiles().get(0).getUrl()).override(100,100).into(preview);
            }  else if(message.getMessageType() == MessageType.RECORDING) {
                messageview.setText("Recording");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_gif_24,0,0,0);
            }  else if(message.getMessageType() == MessageType.DOCUMENT) {
                messageview.setText("Document");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_gif_24,0,0,0);
            }  else if(message.getMessageType() == MessageType.MAP) {
                messageview.setText("Map");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_gif_24,0,0,0);
            }  else if(message.getMessageType() == MessageType.CONTACT) {
                messageview.setText("Contact");
                messageview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_gif_24,0,0,0);
            }  else if(message.getMessageType() == MessageType.STICKER) {
                messageview.setText("Sticker");
                Glide.with(getContext()).asBitmap().load(message.getSharedFiles().get(0).getUrl()).override(100,100).into(preview);
            }   else {
                messageview.setText(message.getMessage());
                preview.setVisibility(GONE);
                messageview.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
            bar.setBackgroundColor(user.getColor());
        }
//        view.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view1) {
//                int index = messageIds.indexOf(message.getMessageId());
//                if(mode == LISTVIEW) {
//                    View item = (View) listView.getItemAtPosition(index);
//                    item.setBackgroundColor(Color.parseColor("#4003A9F4"));
//                    item.setSelected(true);
//                    new Handler().postDelayed(() -> item.setBackgroundColor(Color.TRANSPARENT), 1000);
//                } else {
//                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(index);
//                    if(holder != null) {
//                        View item = holder.itemView;
//                        item.setBackgroundColor(Color.parseColor("#4003A9F4"));
//                        item.setSelected(true);
//                        new Handler().postDelayed(() -> item.setBackgroundColor(Color.TRANSPARENT), 1000);
//                    }
//                }
//
//            }
//        });
        return view;
    }


}
