package com.nobodyknows.chatlistlayoutview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capybaralabs.swipetoreply.ISwipeControllerActions;
import com.capybaralabs.swipetoreply.SwipeController;
import com.nobodyknows.chatlistlayoutview.Adapters.ListViewAdapter;
import com.nobodyknows.chatlistlayoutview.Adapters.RecyclerViewAdapter;
import com.nobodyknows.chatlistlayoutview.Database.DatabaseHelper;
import com.nobodyknows.commonhelper.CONSTANT.MessagePosition;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.nobodyknows.commonhelper.Interfaces.ChatLayoutListener;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.MessageConfiguration;
import com.nobodyknows.commonhelper.Model.User;
import com.nobodyknows.commonhelper.Services.Helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatLayoutView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private Integer RECYCLERVIEW = 0;
    private int mode=0;
    private ArrayList<Message> messages = new ArrayList<>();
    private RelativeLayout root;
    private RecyclerView recyclerView;
    private MessageConfiguration leftMessageConfiguration;
    private MessageConfiguration rightMessageConfiguration;
    private ListView listView;
    private ArrayList<String> dates =new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private ListViewAdapter listViewAdapter;
    private DatabaseHelper databaseHelper;
    private String roomId;
    public static String myId="";
    private Boolean useDatabase = false;
    private Boolean canSave = false;
    private Context mainActivityContext;
    private Integer chatLimit = 30;
    private Boolean playSentAndReceivedSoundEffect = true;
    private int sentSoundEffect = R.raw.message_added;
    private int receivedSoundEffect = R.raw.message_received;
    private ImageView backgroundImage;
    private int offset = 0;
    public static ChatLayoutListener chatLayoutListener;
    public static Map<MessageType,String> downloadPaths = new HashMap<>();
    protected static Helper helper;

    public Context getMainActivityContext() {
        return mainActivityContext;
    }


    private int getNextOffset() {
        return offset+chatLimit;
    }

    public void addUser(User user) {
        this.helper.addUser(user);
    }

    public String getImageDownloadPath(MessageType messageType) {
        return this.downloadPaths.get(messageType);
    }

    public void setDownloadPath(MessageType messageType,String downloadFolder) {
        this.downloadPaths.put(messageType,downloadFolder);
    }
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
        chatLimit = typedArray.getInt(R.styleable.ChatLayoutView_chatLimit,30);
        recyclerView = root.findViewById(R.id.recyclerview);
        listView = root.findViewById(R.id.listview);
        helper = new Helper(getContext());
        helper.setRecyclerView(recyclerView);
        helper.setListView(listView);
        helper.setMode(mode);
        backgroundImage = root.findViewById(R.id.background);
        leftMessageConfiguration = new MessageConfiguration();
        rightMessageConfiguration = new MessageConfiguration();
        leftMessageConfiguration.setMessagePosition(MessagePosition.LEFT);
        leftMessageConfiguration.setBackgroundResource(R.drawable.left_message_drawable);
        rightMessageConfiguration.setMessagePosition(MessagePosition.RIGHT);
        rightMessageConfiguration.setBackgroundResource(R.drawable.right_message_drawable);
    }

    public void initialize(Context mainActivityContext,ChatLayoutListener chatLayoutListener) {
        this.chatLayoutListener = chatLayoutListener;
        this.mainActivityContext = mainActivityContext;
        if(mode == RECYCLERVIEW) {
            continueRecyclerView();
        } else {
            continueListView();
        }
    }


    private void addSwipeRecyclerView() {
        SwipeController controller = new SwipeController(getContext(), new ISwipeControllerActions() {
            @Override
            public void onSwipePerformed(int position) {
                chatLayoutListener.onSwipeToReply(messages.get(position),helper.getReplyMessageView(messages.get(position)));
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(controller);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ListView getListView() {
        return listView;
    }

    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public ListViewAdapter getListViewAdapter() {
        return listViewAdapter;
    }

    private void continueRecyclerView() {
        recyclerView.setVisibility(VISIBLE);
        listView.setVisibility(GONE);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),messages,helper.getUserMap(),downloadPaths);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setInitialPrefetchItemCount(5);
        layoutManager.setRecycleChildrenOnDetach(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);;
        addSwipeRecyclerView();
    }

    private void continueListView() {
        recyclerView.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
        listViewAdapter = new ListViewAdapter(getContext(),0,messages,helper.getUserMap(),downloadPaths);
        listView.setAdapter(listViewAdapter);
    }

    private void notifyAdapter(Boolean scrollToBottom) {
        if(mode == RECYCLERVIEW) {
            recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.getItemCount() -1);
            if(recyclerViewAdapter.getItemCount() > 0 && scrollToBottom) {
                recyclerView.scrollToPosition(recyclerViewAdapter.getItemCount() - 1);
            }
        } else {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    private void correctMessage(Message message) {
        if(message.getMessageType() == MessageType.GIF || message.getMessageType() == MessageType.STICKER) {
            message.setMessage("");
        }
    }

    public void addMessage(Message message) {
        if(!helper.messageIdExists(message.getMessageId())) {
            correctMessage(message);
            message.setRoomId(roomId);
            if(message.getMessageConfiguration() == null) {
                message.setMessageConfiguration(getMessageConfig(message));
            } else {
                if(myId.equalsIgnoreCase(message.getSender())) {
                    message.getMessageConfiguration().setMessagePosition(MessagePosition.RIGHT);
                } else {
                    message.getMessageConfiguration().setMessagePosition(MessagePosition.LEFT);
                    if(playSentAndReceivedSoundEffect) {
                        MediaPlayer.create(getContext(),receivedSoundEffect).start();
                    }
                }
            }
            checkForDate(message,messages,dates);
            if(useDatabase && canSave) {
                databaseHelper.insertMessage(message);
            }
            notifyAdapter(true);
        }
    }

    public Message getMessage(String messageId) {
        if(helper.messageIdExists(messageId)) {
            return messages.get(helper.getMessageIdPositon(messageId));
        }
        return null;
    }

    public void updateMessageStatus(String messageId, MessageStatus newStatus) {
        if(helper.messageIdExists(messageId)) {
            int index = helper.getMessageIdPositon(messageId);
            Message message = messages.get(index);
            if(message.getMessageStatus() != newStatus) {
                message.setMessageStatus(newStatus);
                messages.remove(index);
                messages.add(index,message);
                if(mode == RECYCLERVIEW) {
                    recyclerViewAdapter.notifyItemChanged(index);
                } else {
                    listViewAdapter.notifyDataSetChanged();
                }
                if(playSentAndReceivedSoundEffect && newStatus == MessageStatus.SENT && message.getSender().equalsIgnoreCase(myId)) {
                    MediaPlayer.create(getContext(),sentSoundEffect).start();
                }
                updateMessageToDatabase(message);
            }
        }
    }

    private void updateMessageToDatabase(Message message) {
        if(useDatabase) {
            databaseHelper.updateMessageStatus(message.getMessageId(),message.getMessageStatus());
        }
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return simpleDateFormat.format(date);
    }


    private void checkForDate(Message message,ArrayList<Message> messages,ArrayList<String> dates) {
        String formattedText = getFormattedDate(message.getCreatedTimestamp());
        if(!dates.contains(formattedText)) {
            dates.add(formattedText);
            Message dateMessage = new Message();
            dateMessage.setMessageType(MessageType.DATE);
            if(message.getCreatedTimestamp() == new Date()) {
                formattedText = "Today";
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,-1);
                if(message.getCreatedTimestamp() == calendar.getTime()) {
                    formattedText = "Yesterday";
                }
            }
            dateMessage.setMessageId("DATE_"+message.getCreatedTimestamp());
            dateMessage.setMessage(formattedText+"");
            messages.add(dateMessage);
            helper.addMessageId("DATE_"+message.getCreatedTimestamp());
        }
        if(message.getIsRepliedMessage()) {
            if(message.getReplyMessageView() == null) {
                Message replyMessage = messages.get(helper.getMessageIdPositon(message.getRepliedMessageId()));
                message.setReplyMessageView(helper.getReplyMessageView(replyMessage));
            }
        }
        messages.add(message);
        helper.addMessageId(message.getMessageId());
    }




    private MessageConfiguration getMessageConfig(Message message) {
        if(message.getSender().equals(myId)) {
            return rightMessageConfiguration;
        } else {
            if(playSentAndReceivedSoundEffect) {
                MediaPlayer.create(getContext(),receivedSoundEffect).start();
            }
            return leftMessageConfiguration;
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setIds(String roomId,String myId) {
        this.roomId = roomId;
        this.myId = myId;
        this.helper.setMyId(myId);
        if(useDatabase) {
            canSave = true;
            databaseHelper = new DatabaseHelper(getContext(),roomId);
            databaseHelper.setHelper(helper);
        }
    }

    public void loadAllDBMessage() {
        if(useDatabase) {
            messages.clear();
            notifyAdapter(true);
            messages.addAll(databaseHelper.getAllMessages(myId,leftMessageConfiguration,rightMessageConfiguration,dates));
            notifyAdapter(true);
        }
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

    public void setBackgroundImage(String url) {
        Glide.with(getContext()).load(url).into(backgroundImage);
    }

    public void setBackgroundImage(int resource) {
        Glide.with(getContext()).load(resource).into(backgroundImage);
    }

    public void setBackgroundImage(Uri uri) {
        Glide.with(getContext()).load(uri).into(backgroundImage);
    }

    public void setBackgroundImage(File file) {
        Glide.with(getContext()).load(file).into(backgroundImage);
    }

    public void setBackgroundImage(byte[] bytes) {
        Glide.with(getContext()).load(bytes).into(backgroundImage);
    }

    public void setBackgroundImage(Bitmap bitmap) {
        Glide.with(getContext()).load(bitmap).into(backgroundImage);
    }

    public void setBackgroundImage(Drawable drawable) {
        Glide.with(getContext()).load(drawable).into(backgroundImage);
    }

    public ChatLayoutListener getChatLayoutListener() {
        return chatLayoutListener;
    }

    public void deleteDatabase() {
        this.databaseHelper.deleteAll();
        this.messages.clear();
        helper.clearMessagedIds();
        notifyAdapter(false);
    }

    public void clearCompleteChat() {
        this.databaseHelper.clearAll();
        this.messages.clear();
        helper.clearMessagedIds();
        notifyAdapter(false);
    }

    public Boolean getPlaySentAndReceivedSoundEffect() {
        return playSentAndReceivedSoundEffect;
    }

    public void setPlaySentAndReceivedSoundEffect(Boolean playSentAndReceivedSoundEffect) {
        this.playSentAndReceivedSoundEffect = playSentAndReceivedSoundEffect;
    }

    public int getSentSoundEffect() {
        return sentSoundEffect;
    }

    public void setSentSoundEffect(int sentSoundEffect) {
        this.sentSoundEffect = sentSoundEffect;
    }

    public int getReceivedSoundEffect() {
        return receivedSoundEffect;
    }

    public void setReceivedSoundEffect(int receivedSoundEffect) {
        this.receivedSoundEffect = receivedSoundEffect;
    }
}
