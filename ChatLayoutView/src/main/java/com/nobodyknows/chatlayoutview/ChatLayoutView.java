package com.nobodyknows.chatlayoutview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlayoutview.Adapters.ListViewAdapter;
import com.nobodyknows.chatlayoutview.Adapters.RecyclerViewAdapter;
import com.nobodyknows.chatlayoutview.Database.DatabaseHelper;
import com.nobodyknows.chatmessageview.CONSTANT.MessagePosition;
import com.nobodyknows.chatmessageview.Model.Message;
import com.nobodyknows.chatmessageview.Model.MessageConfiguration;

import java.util.ArrayList;

public class ChatLayoutView extends RelativeLayout {

    private LayoutInflater layoutInflater;
    private Integer RECYCLERVIEW = 0;
    private Integer LISTVIEW = 0;
    private int mode=0;
    private ArrayList<Message> messages = new ArrayList<>();
    private RelativeLayout root;
    private RecyclerView recyclerView;
    private MessageConfiguration leftMessageConfiguration;
    private MessageConfiguration rightMessageConfiguration;
    private ListView listView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ListViewAdapter listViewAdapter;
    private DatabaseHelper databaseHelper;
    private String roomId,myId="";
    private Boolean useDatabase = false;
    private Boolean canSave = false;
    public ChatLayoutView(Context context) {
        super(context);
        init(null,0);
    }

    public ChatLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ChatLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = (RelativeLayout) layoutInflater.inflate(R.layout.chatlayout,this,true);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.ChatLayoutView);
        mode = typedArray.getInt(R.styleable.ChatLayoutView_view_mode,0);
        useDatabase = typedArray.getBoolean(R.styleable.ChatLayoutView_useDatabase,false);
        recyclerView = root.findViewById(R.id.recyclerview);
        listView = root.findViewById(R.id.listview);
        leftMessageConfiguration = new MessageConfiguration();
        rightMessageConfiguration = new MessageConfiguration();
        leftMessageConfiguration.setMessagePosition(MessagePosition.LEFT);
        rightMessageConfiguration.setMessagePosition(MessagePosition.RIGHT);
        if(mode == RECYCLERVIEW) {
            continueRecyclerView();
        } else {
            continueListView();
        }
    }

    private void continueRecyclerView() {
        recyclerView.setVisibility(VISIBLE);
        listView.setVisibility(GONE);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void continueListView() {
        recyclerView.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
        listViewAdapter = new ListViewAdapter(getContext(),R.layout.message_box,messages);
        listView.setAdapter(listViewAdapter);
    }

    private void notifyAdapter() {
        if(mode == RECYCLERVIEW) {
            recyclerViewAdapter.notifyDataSetChanged();
            if(recyclerViewAdapter.getItemCount() > 0) {
                recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount() - 1);
            }
        } else {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        message.setRoomId(roomId);
        message.setMessageConfiguration(getMessageConfig(message));
        messages.add(message);
        if(useDatabase && canSave) {
            databaseHelper.insertMessage(message);
        }
        notifyAdapter();
    }

    private MessageConfiguration getMessageConfig(Message message) {
        if(message.getSender().equals(myId)) {
            return rightMessageConfiguration;
        } else {
            return leftMessageConfiguration;
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setIds(String roomId,String myId) {
        this.roomId = roomId;
        this.myId = myId;
        if(useDatabase) {
            canSave = true;
            databaseHelper = new DatabaseHelper(getContext(),roomId);
            loadAllMessage();
        }
    }

    public void loadAllMessage() {
        messages.clear();
        notifyAdapter();
        messages.addAll(databaseHelper.getAllMessages(myId,leftMessageConfiguration,rightMessageConfiguration));
        notifyAdapter();
    }

    public MessageConfiguration getLeftMessageConfiguration() {
        return leftMessageConfiguration;
    }

    public void setLeftMessageConfiguration(MessageConfiguration leftMessageConfiguration) {
        this.leftMessageConfiguration = leftMessageConfiguration;
    }

    public MessageConfiguration getRightMessageConfiguration() {
        return rightMessageConfiguration;
    }

    public void setRightMessageConfiguration(MessageConfiguration rightMessageConfiguration) {
        this.rightMessageConfiguration = rightMessageConfiguration;
    }

    public String getMyId() {
        return myId;
    }
}
