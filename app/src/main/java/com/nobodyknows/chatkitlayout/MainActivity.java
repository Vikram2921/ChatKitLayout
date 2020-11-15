package com.nobodyknows.chatkitlayout;

import android.content.ClipDescription;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import com.nobodyknows.chatlistlayoutview.ChatLayoutView;
import com.nobodyknows.chatlistlayoutview.Services.LayoutService;
import com.nobodyknows.commonhelper.CONSTANT.MessagePosition;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.nobodyknows.commonhelper.Interfaces.ChatLayoutListener;
import com.nobodyknows.commonhelper.Model.Contact;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.MessageConfiguration;
import com.nobodyknows.commonhelper.Model.SharedFile;
import com.nobodyknows.commonhelper.Model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ChatLayoutListener {

    int i=0;
    Boolean viewAdded = false;
    LinearLayout linearLayout;
    Message selmessage;
    private Boolean haveLink = false;
    View selView;
    ChatLayoutView chatLayoutView;
    ArrayList<String> ids = new ArrayList<>(Arrays.asList("7014550298","8442000360"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.viewhold);
        LayoutService.initialize(getApplicationContext());
        chatLayoutView = findViewById(R.id.chatlayout_view);
        chatLayoutView.initialize(getApplicationContext(),this);
        chatLayoutView.setDownloadPath(MessageType.IMAGE,"/ChatKitLayout/Images");
        chatLayoutView.setDownloadPath(MessageType.VIDEO,"/ChatKitLayout/Videos");
        chatLayoutView.setDownloadPath(MessageType.AUDIO,"/ChatKitLayout/Audios");
        chatLayoutView.setDownloadPath(MessageType.RECORDING,"/ChatKitLayout/Recordings");
        chatLayoutView.setDownloadPath(MessageType.GIF,"/ChatKitLayout/Gif");
        chatLayoutView.setDownloadPath(MessageType.DOCUMENT,"/ChatKitLayout/Documents");
        chatLayoutView.setIds("ROOM1","7014550298");
        User myUserObject = new User();
        myUserObject.setName("Vikram Singh Rawat");
        myUserObject.setColor(Color.BLUE);
        myUserObject.setProfileUrl("https://static.toiimg.com/thumb/msid-77797046,width-1200,height-900,resizemode-4/.jpg");
        myUserObject.setUserId("7014550298");
        User freindUserObject = new User();
        freindUserObject.setName("Pritam Singh Rathore");
        freindUserObject.setProfileUrl("https://upload.wikimedia.org/wikipedia/commons/0/0c/Sunny_Leone_for_%27Sunny_Ka_New_Year_Call%27_campaign.jpg");
        freindUserObject.setColor(-806962);
        freindUserObject.setUserId("8442000360");
        chatLayoutView.addUser(myUserObject);
        chatLayoutView.addUser(freindUserObject);
        chatLayoutView.loadAllDBMessage();
        Button button = findViewById(R.id.clickme);
        EditText editText = findViewById(R.id.message);
        editText.setAutoLinkMask(Linkify.ALL);
        MessageConfiguration messageConfiguration = new MessageConfiguration();
        messageConfiguration.setBackgroundResource(R.drawable.left_message_drawable_demo);
        chatLayoutView.setRightMessageConfiguration(messageConfiguration);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(LayoutService.containsURL(s.toString())) {
                    haveLink = true;
                } else {
                    haveLink = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    Message message;
                    if(viewAdded) {
                        message = getReplyMessages(editText.getText().toString().trim(),new Random().nextInt(9999)+"",selmessage.getMessageId());
                        linearLayout.removeViewAt(0);
                        viewAdded = false;
                    }
                    else {
                        message = getMessages(editText.getText().toString().trim(),new Random().nextInt(9999)+"",false);
                    }
                    if(haveLink) {
                        message.setMessageType(MessageType.LINK_TEXT_VIEW);
                        haveLink = false;
                    }
                    chatLayoutView.addMessage(message);
                    editText.setText("");
                    changeStatusafterTime(message);
                }
            }
        });
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));

        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getImageMessages("Single Image View",Arrays.asList("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg","https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg")));
        chatLayoutView.addMessage(getInfoMessages("Messaged and calls are ene-to-end encrypted. No one outside of this chat,Not even ChatMe, can read or listen to them. Tap to learn more","rightInfo",false,"7014550298","8014550298"));
        //chatLayoutView.addMessage(getGifMessage(""));
        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/9Dk1ba2smFg2KASTcz/200.webp",8));
        chatLayoutView.addMessage(getContactMessage("https://i.giphy.com/media/9Dk1ba2smFg2KASTcz/200.webp",false));
        chatLayoutView.addMessage(getContactMessage("https://i.giphy.com/media/9Dk1ba2smFg2KASTcz/200.webp",false));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/3oFzmeVbeXIfBUl5sI/giphy.webp",10));
        chatLayoutView.addMessage(getContactMessage("12345678",false));
        chatLayoutView.addMessage(getAudioMessages("12345678","112312",false,""));
        chatLayoutView.addMessage(getAudioMessages("12345678","112316",false,""));
        chatLayoutView.addMessage(getVideoMessages(""));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
//        chatLayoutView.addMessage(getRecordingMessages("111222333"+i++,false,"https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/On%20My%20Way%20-%20Alan%20Walker%20128%20Kbps(PagalWorldCom.Com).mp3?alt=media&token=65b58e7f-7bb0-498e-9c3f-3d93d2de108a"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LayoutService.destroyPlayer();
    }



    private void changeStatusafterTime(Message message) {
        chatLayoutView.updateMessageStatus(message.getMessageId(), MessageStatus.SENT);
    }


    private Message getContactMessage( String s, boolean b) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(s);
        message.setIsRepliedMessage(b);
        message.setMessageType(MessageType.CONTACT);
        message.setMessage("");
        message.setSender(random);
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.addContact(getContact("Vikram","7014550298"));
        message.setMessageStatus(MessageStatus.SENT);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());

        message.setReceivedAt(new Date());
        return message;
    }

    private Contact getContact(String name, String number) {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setContactNumbers(number);
        return contact;
    }

    private Message getDocumentMessage(String string, String s, boolean b, String url) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(s);
        message.setIsRepliedMessage(b);
        message.setMessageType(MessageType.DOCUMENT);
        message.addSharedFile(getSharedFile(url,message.getMessageId()+"_"+i,"mp3"));
        message.setMessage("");
        message.setSender(random);
        message.setMessageStatus(MessageStatus.SENT);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getRecordingMessages(String id, boolean isReplied, String url) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(id);
        message.setIsRepliedMessage(isReplied);
        message.setMessageType(MessageType.RECORDING);
        message.addSharedFile(getSharedFile(url,message.getMessageId()+"_"+i,"mp3"));
        message.setMessage("");
        message.setSender(random);
        message.setMessageStatus(MessageStatus.SENT);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getAudioMessages(String string, String s, boolean b,String url) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(s);
        message.setIsRepliedMessage(b);
        message.setMessageType(MessageType.AUDIO);
        message.addSharedFile(getSharedFile("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3",message.getMessageId()+"_"+i,"mp3"));
        message.setMessage("");
        message.setSender(random);
        message.setMessageStatus(MessageStatus.SENT);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getMessages(String messageText,String id,Boolean isReplied) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(id);
        message.setIsRepliedMessage(isReplied);
        message.setRepliedMessageId("112");
        if(isReplied) {
            message.setMessageType(MessageType.VIDEO);
            message.addSharedFile(getSharedFile("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",message.getMessageId()+"_"+i++,"jpg"));
        }
        message.setMessage(messageText);
        message.setSender(random);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getInfoMessages(String messageText,String id,Boolean isReplied,String sender,String reciver) {
        //String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(id);
        message.setMessageType(MessageType.WARNING);
        message.setIsRepliedMessage(isReplied);
        message.setRepliedMessageId("1126");
        if(isReplied) {
            message.setMessageType(MessageType.VIDEO);
            message.addSharedFile(getSharedFile("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",message.getMessageId()+"_"+i++,"jpg"));
        }
        message.setMessage(messageText);
        message.setSender(sender);
        message.setReceiver(reciver);
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getReplyMessages(String messageText,String id,String replyMessageId) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(id);
        message.setIsRepliedMessage(true);
        message.setRepliedMessageId(replyMessageId);
        message.setMessageType(MessageType.TEXT);
        message.addSharedFile(getSharedFile("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",message.getMessageId()+"_"+i,"jpg"));
        message.setMessage(messageText);
        message.setSender(random);
        message.setReplyMessageView(selView);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getImageMessages(String messageText,List<String> urls) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.IMAGE);
        message.setMessageId("27517"+i++);
        message.setMessage(messageText);
        message.setSender(random);
        for(int i=0;i<urls.size();i++) {
            message.addSharedFile(getSharedFile(urls.get(i),message.getMessageId()+"_"+i,"jpg"));
        }
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getVideoMessages(String messageText) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.VIDEO);
        message.setMessageId("275199"+i++);
        message.setMessage(messageText);
        message.setSender(random);
        message.addSharedFile(getSharedFile("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",message.getMessageId()+"5","mp4"));
        message.addSharedFile(getSharedFile("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4",message.getMessageId()+"0","mp4"));
        message.addSharedFile(getSharedFile("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4",message.getMessageId()+"1","mp4"));
        message.addSharedFile(getSharedFile("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",message.getMessageId()+"2","mp4"));
        message.addSharedFile(getSharedFile("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_10mb.mp4",message.getMessageId()+"3","mp4"));
        message.addSharedFile(getSharedFile("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_20mb.mp4",message.getMessageId()+"4","mp4"));
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getGifMessage(String messageText,String url) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.GIF);
        message.setMessageId("2751300");
        message.setMessage("");
        message.setSender(random);
        message.addSharedFile(getSharedFile(url,message.getMessageId()+"5","gif"));
        //        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getStickerMessage(String messageText,int i) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.STICKER);
        message.setMessageId("2751400"+i);
        message.setMessage("");
        message.setSender(random);
        message.addSharedFile(getSharedFile(messageText,message.getMessageId()+"5","webp"));
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        if(i==5) {
            MessageConfiguration messageConfiguration = new MessageConfiguration();
            messageConfiguration.setMessagePosition(MessagePosition.RIGHT);
            messageConfiguration.setBackgroundResource(R.drawable.left_message_drawable_demo);
            message.setMessageConfiguration(messageConfiguration);
        }
        return message;
    }

    private SharedFile getSharedFile(String url, String name, String exetension) {
        SharedFile sharedFile = new SharedFile();
        sharedFile.setFileId("FILE_"+name+"_7014550298_"+new Random().nextInt(9999));
        sharedFile.setSize(10000.0);
        sharedFile.setDuration(0.0);
        sharedFile.setExtension(exetension);
        sharedFile.setName(name);
        sharedFile.setUrl(url);
        //sharedFile.setPreviewUrl("https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/sunny-compressed.jpg?alt=media&token=6a7aa85b-9bee-4bf7-89d0-c982e45bc71b");

        return sharedFile;
    }

    @Override
    public void onSwipeToReply(Message message,View replyView) {
        if(viewAdded) {
            linearLayout.removeViewAt(0);
        }
        selView = replyView;
        linearLayout.addView(replyView,0);
        viewAdded =true;
        selmessage = message;
    }

    @Override
    public void onUploadRetry(Message message) {
        Toast.makeText(getApplicationContext(),message.getMessageId(),Toast.LENGTH_SHORT).show();
    }
}