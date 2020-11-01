package com.nobodyknows.chatkitlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.ChatLayoutView;
import com.nobodyknows.chatlayoutview.Interfaces.ChatLayoutListener;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.SharedFile;
import com.nobodyknows.chatlayoutview.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ChatLayoutListener {

    int i=0;
    ArrayList<String> ids = new ArrayList<>(Arrays.asList("7014550298","8442000360"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChatLayoutView chatLayoutView = findViewById(R.id.chatlayout_view);
        chatLayoutView.setChatLayoutListener(this);
        chatLayoutView.setActivity(MainActivity.this);
        chatLayoutView.setDownloadPath(MessageType.IMAGE,"/ChatKitLayout/Images");
        chatLayoutView.setDownloadPath(MessageType.VIDEO,"/ChatKitLayout/Videos");
        chatLayoutView.setIds("ROOM1","7014550298");
        User myUserObject = new User();
        myUserObject.setName("Vikram Singh Rawat");
        myUserObject.setColor(Color.MAGENTA);
        myUserObject.setUserId("7014550298");
        User freindUserObject = new User();
        freindUserObject.setName("Pritam Singh Rathore");
        freindUserObject.setProfileUrl("");
        freindUserObject.setColor(Color.RED);
        freindUserObject.setUserId("8442000360");
        chatLayoutView.addUser(myUserObject);
        chatLayoutView.addUser(freindUserObject);
        chatLayoutView.setBackgroundImage("https://wallpaperaccess.com/full/1288076.jpg");
        Button button = findViewById(R.id.clickme);
        EditText editText = findViewById(R.id.message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    chatLayoutView.addMessage(getMessages(editText.getText().toString().trim()));
                    editText.setText("");
                }
            }
        });
       // chatLayoutView.addMessage(getImageMessages("This is an example of image chat"));
        chatLayoutView.addMessage(getVideoMessages("This is an example of Video chat"));
    }

    private Message getMessages(String messageText) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(new Random().nextInt(999999) +"");
        message.setMessage(messageText);
        message.setSender(random);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
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

    private SharedFile getSharedFile(String url,String name,String exetension) {
        SharedFile sharedFile = new SharedFile();
        sharedFile.setExtension(exetension);
        sharedFile.setName(name);
        sharedFile.setUrl(url);
        return sharedFile;
    }

    @Override
    public void onSwipeToReply(Message message) {
        Toast.makeText(getApplicationContext(),message.getMessage(),Toast.LENGTH_LONG).show();
    }
}