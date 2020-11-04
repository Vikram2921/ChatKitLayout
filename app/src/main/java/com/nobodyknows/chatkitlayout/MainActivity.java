package com.nobodyknows.chatkitlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nobodyknows.chatlayoutview.CONSTANT.MessagePosition;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageStatus;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.ChatLayoutView;
import com.nobodyknows.chatlayoutview.Interfaces.ChatLayoutListener;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.MessageConfiguration;
import com.nobodyknows.chatlayoutview.Model.SharedFile;
import com.nobodyknows.chatlayoutview.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ChatLayoutListener {

    int i=0;
    Boolean viewAdded = false;
    LinearLayout linearLayout;
    Message selmessage;
    View selView;
    ArrayList<String> ids = new ArrayList<>(Arrays.asList("7014550298","8442000360"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.viewhold);
        ChatLayoutView chatLayoutView = findViewById(R.id.chatlayout_view);
        chatLayoutView.setChatLayoutListener(this);
        chatLayoutView.setActivity(MainActivity.this);
        chatLayoutView.setDownloadPath(MessageType.IMAGE,"/ChatKitLayout/Images");
        chatLayoutView.setDownloadPath(MessageType.VIDEO,"/ChatKitLayout/Videos");
        chatLayoutView.setDownloadPath(MessageType.GIF,"/ChatKitLayout/Gif");
        chatLayoutView.setIds("ROOM1","7014550298");
        User myUserObject = new User();
        myUserObject.setName("Vikram Singh Rawat");
        myUserObject.setColor(Color.BLUE);
        myUserObject.setUserId("7014550298");
        User freindUserObject = new User();
        freindUserObject.setName("Pritam Singh Rathore");
        freindUserObject.setProfileUrl("");
        freindUserObject.setColor(Color.RED);
        freindUserObject.setUserId("8442000360");
        chatLayoutView.addUser(myUserObject);
        chatLayoutView.addUser(freindUserObject);
        chatLayoutView.loadAllDBMessage();
        //chatLayoutView.setBackgroundImage("https://wallpaperaccess.com/full/1288076.jpg");
        Button button = findViewById(R.id.clickme);
        EditText editText = findViewById(R.id.message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    if(viewAdded) {
                        chatLayoutView.addMessage(getReplyMessages(editText.getText().toString().trim(),new Random().nextInt(9999)+"",selmessage.getMessageId()));
                        linearLayout.removeViewAt(0);
                        viewAdded = false;
                    } else {
                        chatLayoutView.addMessage(getMessages(editText.getText().toString().trim(),new Random().nextInt(9999)+"",false));
                    }
                    editText.setText("");
                }
            }
        });
        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message and this is goona be a long message","112",false));
        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","114",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","115",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","116",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","117",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","118",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","119",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","120",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","121",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","122",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","123",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","124",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","125",false));
//        chatLayoutView.addMessage(getMessages("This is and example to reply messsage this message is goona be reply to another message","126",false));
        chatLayoutView.addMessage(getMessages(getResources().getString(R.string.dummy_text),"113",true));
      //  chatLayoutView.addMessage(getImageMessages("This is an example of IMAGE chat"));
      //  chatLayoutView.addMessage(getVideoMessages("This is an example of VIDEO chat"));
      //  chatLayoutView.addMessage(getGifMessage("This is an example of GIF chat"));
        int k=0;
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/FCYNQLacZGwFTNaIIT/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/7JmuMGOZYJYmeivT8j/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
////        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
//        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/kEcADJ0v0ONJa02x5C/giphy.webp",k++));
    }

    private Message getMessages(String messageText,String id,Boolean isReplied) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(id);
        message.setIsRepliedMessage(isReplied);
        message.setRepliedMessageId("112");
        if(isReplied) {
            message.setMessageType(MessageType.VIDEO);
            message.addSharedFile(getSharedFile("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",message.getMessageId()+"_"+i,"jpg"));
        }
        message.setMessage(messageText);
        message.setSender(random);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
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
        MessageConfiguration messageConfiguration = new MessageConfiguration();
        messageConfiguration.setTextColor(Color.WHITE);
        messageConfiguration.setTimeTextColor(Color.WHITE);
        messageConfiguration.setMessagePosition(MessagePosition.RIGHT);
        messageConfiguration.setBackgroundResource(R.drawable.left_message_drawable_demo);
        message.setMessageConfiguration(messageConfiguration);
        return message;
    }

    private Message getImageMessages(String messageText) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.IMAGE);
        message.setMessageId("27517");
        message.setMessage(messageText);
        message.setSender(random);
        ArrayList<String> urls =new ArrayList<>();
        urls.add("https://www.theindianwire.com/wp-content/uploads/2019/08/Sunny-Leone2.jpg");
        urls.add("https://www.india.com/wp-content/uploads/2016/12/16-7.jpg");
        urls.add("https://static.india.com/wp-content/uploads/2020/02/Sunny-Leone-5.jpg?impolicy=Medium_Resize&w=1200&h=800");
        urls.add("https://assets.gqindia.com/photos/5e5de88e15f90c0008bcb960/master/pass/Exclusive-This-is-what-Katrina-Kaif-eats-through-the-day-to-stay-in-her-ever-so-great-shape.jpg");
        urls.add("https://indianewengland.com/wp-content/uploads/2020/02/Katrina-Kaif-Facebook.jpg");
        urls.add("https://caknowledge.com/wp-content/uploads/2020/02/Katrina-Kaif.jpeg");
        urls.add("https://images.squarespace-cdn.com/content/v1/50f6d8dfe4b0c4007db424a3/1585141927325-PA6NTIQY0JGGQZIKX4YR/ke17ZwdGBToddI8pDm48kD3l6gdMBdO-PyPu-zZmu4kUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKc47Ll6mSOzQH6DpuxFLzePoWDoqw8MA2UO1fAR9rtw_xoc41MWnq4at-qR6eYNwYU/Katrina-Kaif-Bharat.jpg");
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
        message.setMessageId("275199");
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

    private Message getGifMessage(String messageText) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.GIF);
        message.setMessageId("2751300");
        message.setMessage(messageText);
        message.setSender(random);
        message.addSharedFile(getSharedFile("https://media3.giphy.com/media/26ybvqyqoH7UbyHeg/giphy.gif",message.getMessageId()+"5","gif"));
        message.addSharedFile(getSharedFile("https://media4.giphy.com/media/3o6YgaqfCPku7lUSoE/giphy.gif",message.getMessageId()+"6","gif"));
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Message getStickerMessage(String messageText,int i) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageType(MessageType.STICKER);
        message.setMessageStatus(MessageStatus.DELETED);
        message.setMessageId("2751400"+i);
        message.setMessage(messageText);
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

    private SharedFile getSharedFile(String url,String name,String exetension) {
        SharedFile sharedFile = new SharedFile();
        sharedFile.setExtension(exetension);
        sharedFile.setName(name);
        sharedFile.setUrl(url);
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
}