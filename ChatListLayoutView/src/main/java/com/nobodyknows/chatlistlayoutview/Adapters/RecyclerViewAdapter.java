package com.nobodyknows.chatlistlayoutview.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;
import com.nobodyknows.chatlistlayoutview.R;
import com.nobodyknows.chatlistlayoutview.ViewHolders.DateAndInfoView;
import com.nobodyknows.chatlistlayoutview.ViewHolders.LinkTextMessageViewLeft;
import com.nobodyknows.chatlistlayoutview.ViewHolders.LinkTextMessageViewRight;
import com.nobodyknows.chatlistlayoutview.ViewHolders.TextMessageViewLeft;
import com.nobodyknows.chatlistlayoutview.ViewHolders.TextMessageViewRight;
import com.nobodyknows.chatlistlayoutview.ViewHolders.WarningView;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.User;

import java.util.ArrayList;
import java.util.Map;

import static com.nobodyknows.chatlistlayoutview.ChatLayoutView.myId;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> messages;
    private Context context;
    private Map<String, User> userMap;
    private Map<MessageType,String> downloadPath;
    private MediaPlayer mediaPlayer;
    private ChatLinkDatabaseHelper chatLinkDatabaseHelper;

    //*************EXTRAS **************//
    private final int DATE_AND_INFO_MESSAGE = 00;
    private final int WARNNIG_MESSAGE = 01;
    //***********TEXT ***************//
    private final int SENT_TEXT_MESSAGE = 11;
    private final int SENT_LINK_TEXT_MESSAGE = 12;
    private final int RECEIVE_TEXT_MESSAGE = 13;
    private final int RECEIVE_LINK_TEXT_MESSAGE = 14;
    //**************IMAGES *************//
    private final int SENT_SINGLE_IMAGE = 21;
    private final int SENT_MULTIPLE_IMAGES = 22;
    private final int RECEIVE_SINGLE_IMAGE = 23;
    private final int RECEIVE_MULTIPLE_IMAGES = 24;

    public RecyclerViewAdapter(Context context, ArrayList<Message> messages, Map<String, User> userMap, Map<MessageType,String> downloadPaths, MediaPlayer mediaPlayer, ChatLinkDatabaseHelper chatLinkDatabaseHelper) {
        this.context = context;
        this.messages = messages;
        this.mediaPlayer = mediaPlayer;
        this.userMap = userMap;
        this.chatLinkDatabaseHelper = chatLinkDatabaseHelper;
        this.downloadPath = downloadPaths;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = null;
        RecyclerView.ViewHolder  viewHolder = null;
        if(viewType == DATE_AND_INFO_MESSAGE) {
            item = layoutInflater.inflate(R.layout.extra_info_view,parent,false);
            viewHolder = new DateAndInfoView(item);
        } else if(viewType == WARNNIG_MESSAGE) {
            item = layoutInflater.inflate(R.layout.warning_view,parent,false);
            viewHolder = new WarningView(item);
        } else if(viewType == SENT_TEXT_MESSAGE) {
            item = layoutInflater.inflate(R.layout.messageview_right_text,parent,false);
            viewHolder = new TextMessageViewRight(item);
        } else if(viewType == RECEIVE_TEXT_MESSAGE) {
            item = layoutInflater.inflate(R.layout.messageview_left_text,parent,false);
            viewHolder = new TextMessageViewLeft(item);
        } else if(viewType == SENT_LINK_TEXT_MESSAGE) {
            item = layoutInflater.inflate(R.layout.messageview_right_linktext,parent,false);
            viewHolder = new LinkTextMessageViewRight(item);
        } else if(viewType == RECEIVE_LINK_TEXT_MESSAGE) {
            item = layoutInflater.inflate(R.layout.messageview_left_linktext,parent,false);
            viewHolder = new LinkTextMessageViewLeft(item);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        switch (holder.getItemViewType()){
            case DATE_AND_INFO_MESSAGE:
                ((DateAndInfoView) holder).initalize(message.getMessage());
                break;
            case WARNNIG_MESSAGE:
                ((WarningView) holder).initalize(message.getMessage());
                break;
            case SENT_TEXT_MESSAGE:
                ((TextMessageViewRight) holder).initalize(message,chatLinkDatabaseHelper);
                break;
            case RECEIVE_TEXT_MESSAGE:
                ((TextMessageViewLeft) holder).initalize(message,userMap.get(message.getSender()),chatLinkDatabaseHelper);
                break;
            case SENT_LINK_TEXT_MESSAGE:
                ((LinkTextMessageViewRight) holder).initalize(message,chatLinkDatabaseHelper);
                break;
            case RECEIVE_LINK_TEXT_MESSAGE:
                ((LinkTextMessageViewLeft) holder).initalize(message,userMap.get(message.getSender()),chatLinkDatabaseHelper);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
       Message message = messages.get(position);
       if(message.getMessageType() == MessageType.DATE || message.getMessageType() == MessageType.INFO) {
           return DATE_AND_INFO_MESSAGE;
       } else if(message.getMessageType() == MessageType.WARNING) {
           return WARNNIG_MESSAGE;
       } else {
           if(message.getSender() != null && message.getSender().length() > 0 && message.getSender().equalsIgnoreCase(myId)) {
               if(message.getMessageType() == MessageType.TEXT) {
                   return SENT_TEXT_MESSAGE;
               } else  if(message.getMessageType() == MessageType.LINK_TEXT_VIEW) {
                   return SENT_LINK_TEXT_MESSAGE;
               }
           } else {
               if(message.getMessageType() == MessageType.TEXT) {
                   return RECEIVE_TEXT_MESSAGE;
               } else if(message.getMessageType() == MessageType.LINK_TEXT_VIEW) {
                   return RECEIVE_LINK_TEXT_MESSAGE;
               }
           }
       }
       return -1;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
