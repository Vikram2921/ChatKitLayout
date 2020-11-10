package com.nobodyknows.chatuserlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatuserlistview.Adapters.RecyclerViewAdapter;
import com.nobodyknows.chatuserlistview.Database.DatabaseHelper;
import com.nobodyknows.chatuserlistview.Model.User;

import java.util.ArrayList;
import java.util.Date;

public class ChatUserListView extends RelativeLayout {

    private LayoutInflater layoutInflater;
    private Boolean useDatabase = false;
    public static String myId;
    private RelativeLayout root;
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> usersIds = new ArrayList<>();
    public ChatUserListView(Context context) {
        super(context);
        init(null,0);
    }
    public ChatUserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }
    public ChatUserListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = (RelativeLayout) layoutInflater.inflate(R.layout.chat_user_list,this,true);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.ChatUserListView);
        useDatabase = typedArray.getBoolean(R.styleable.ChatUserListView_useDatabase,false);
        if(useDatabase) {
            databaseHelper = new DatabaseHelper(getContext());
        }
        continueRecyclerView();
    }

    private void continueRecyclerView() {
        recyclerView = findViewById(R.id.userlistview);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),users);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setInitialPrefetchItemCount(5);
        layoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void addUser(User user) {
//        if(!usersIds.contains(user.getUserId()))
        {
            user.setName(user.getName());
            usersIds.add(0,user.getUserId());
            users.add(0,user);
            recyclerViewAdapter.notifyItemInserted(0);
        }
    }

    public void updateLastMessage(String userId, String lastMessage, String lastMessageSender, Date lastMessageDate, MessageStatus lastMessageStatus) {
        databaseHelper.updateUser(userId,lastMessage,lastMessageSender,lastMessageDate,lastMessageStatus);
        changeToTop(userId);
    }

    private void changeToTop(String userId) {
        int index = usersIds.indexOf(userId);
        User user = users.get(index);
        usersIds.remove(index);
        users.remove(index);
        usersIds.add(0,user.getUserId());
        users.add(0,user);
        recyclerViewAdapter.notifyItemMoved(index,0);
    }

}
