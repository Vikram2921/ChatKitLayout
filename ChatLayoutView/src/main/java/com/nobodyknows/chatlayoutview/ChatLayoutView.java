package com.nobodyknows.chatlayoutview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capybaralabs.swipetoreply.ISwipeControllerActions;
import com.capybaralabs.swipetoreply.SwipeController;
import com.nobodyknows.chatlayoutview.Adapters.ListViewAdapter;
import com.nobodyknows.chatlayoutview.Adapters.RecyclerViewAdapter;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.Database.DatabaseHelper;
import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;
import com.nobodyknows.chatlayoutview.Interfaces.ChatLayoutListener;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.MessageConfiguration;
import com.nobodyknows.chatlayoutview.Model.User;

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
    private Integer LISTVIEW = 0;
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
    private String roomId,myId="";
    private Boolean useDatabase = false;
    private Boolean canSave = false;
    private ArrayList<String> messageIds = new ArrayList<>();
    private Integer chatLimit = 30;
    private ImageView backgroundImage;
    private int offset = 0;
    private boolean dynamicScrolling = false;
    private ChatLayoutListener chatLayoutListener;
    private Map<String, User> usermap = new HashMap<>();
    public static DownloadHelper downloadHelper;
    private Map<MessageType,String> downloadPaths = new HashMap<>();

    private int getNextOffset() {
        return offset+chatLimit;
    }

    public void addUser(User user) {
        usermap.put(user.getUserId(),user);
    }

    public String getImageDownloadPath(MessageType messageType) {
        return this.downloadPaths.get(messageType);
    }

    public void setDownloadPath(MessageType messageType,String downloadFolder) {
        this.downloadPaths.put(messageType,downloadFolder);
    }

    public User getUser(String userId) {
        return usermap.get(userId);
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

    public void setActivity(Activity activity) {
        downloadHelper = new DownloadHelper(getContext(),activity);
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
        backgroundImage = root.findViewById(R.id.background);
        leftMessageConfiguration = new MessageConfiguration();
        rightMessageConfiguration = new MessageConfiguration();
        leftMessageConfiguration.setMessagePosition(MessagePosition.LEFT);
        leftMessageConfiguration.setBackgroundResource(R.drawable.left_message_drawable);
        rightMessageConfiguration.setMessagePosition(MessagePosition.RIGHT);
        rightMessageConfiguration.setBackgroundResource(R.drawable.right_message_drawable);
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
                chatLayoutListener.onSwipeToReply(messages.get(position),getReplyMessageView(messages.get(position)));
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(controller);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void continueRecyclerView() {
        recyclerView.setVisibility(VISIBLE);
        listView.setVisibility(GONE);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),messages,usermap,downloadPaths);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Toast.makeText(getContext(),"BOTTOM LAST",Toast.LENGTH_LONG).show();
//                }
                if(!recyclerView.canScrollVertically(-1)  && newState == RecyclerView.SCROLL_STATE_IDLE && dynamicScrolling) {
                    messages.addAll(0,databaseHelper.getLimitedMessages(myId,leftMessageConfiguration,rightMessageConfiguration,chatLimit,getNextOffset()));
                    notifyAdapter(false);
                }
            }
        });
        addSwipeRecyclerView();
    }

    private void continueListView() {
        recyclerView.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
        listViewAdapter = new ListViewAdapter(getContext(),R.layout.message_box,messages,usermap,downloadPaths);
        listView.setAdapter(listViewAdapter);
    }

    private void notifyAdapter(Boolean scrollToBottom) {
        if(mode == RECYCLERVIEW) {
            recyclerViewAdapter.notifyDataSetChanged();
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

    public void addMessage(Message message) {
        if(!messageIds.contains(message.getMessageId())) {
            message.setRoomId(roomId);
            if(message.getMessageConfiguration() == null) {
                message.setMessageConfiguration(getMessageConfig(message));
            } else {
                if(myId.equalsIgnoreCase(message.getSender())) {
                    message.getMessageConfiguration().setMessagePosition(MessagePosition.RIGHT);
                } else {
                    message.getMessageConfiguration().setMessagePosition(MessagePosition.LEFT);
                }
            }
            checkForDate(message,messages,dates);
            if(useDatabase && canSave) {
                databaseHelper.insertMessage(message);
            }
            notifyAdapter(true);
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
            dateMessage.setMessageId("DATE_"+formattedText);
            dateMessage.setMessage(formattedText+"");
            messages.add(dateMessage);
            messageIds.add("DATE_"+message.getCreatedTimestamp());
        }
        if(message.getIsRepliedMessage()) {
            if(message.getReplyMessageView() == null) {
                Message replyMessage = messages.get(messageIds.indexOf(message.getRepliedMessageId()));
                message.setReplyMessageView(getReplyMessageView(replyMessage));
            }
        }
        messages.add(message);
        messageIds.add(message.getMessageId());
    }

    private View getReplyMessageView(Message message) {
        View view = layoutInflater.inflate(R.layout.replyview,null);
        ImageView preview = view.findViewById(R.id.preview);
        TextView senderName = view.findViewById(R.id.sendername);
        TextView messageview = view.findViewById(R.id.message);
        View bar = view.findViewById(R.id.bar);
        Log.d("TAGUSERMAP", "getReplyMessageView: "+usermap+" \n"+message.getSender());
        if(message.getMessageType() == MessageType.DATE) {
            senderName.setText("Date");
            messageview.setText(message.getMessage());
            senderName.setTextColor(Color.BLACK);
            bar.setBackgroundColor(Color.BLACK);
            preview.setVisibility(GONE);
        } else {
            User user = usermap.get(message.getSender());
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
        notifyAdapter(true);
        messages.addAll(databaseHelper.getAllMessages(myId,leftMessageConfiguration,rightMessageConfiguration,dates,messageIds));
        notifyAdapter(true);
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

    public void setChatLayoutListener(ChatLayoutListener chatLayoutListener) {
        this.chatLayoutListener = chatLayoutListener;
    }
}
