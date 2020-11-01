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
        chatLayoutView.addMessage(getImageMessages("This is an example of image chat"));
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
        message.setMessageId(new Random().nextInt(999999) +"");
        message.setMessage(messageText);
        message.setSender(random);
        ArrayList<String> urls =new ArrayList<>();
        urls.add("https://www.theindianwire.com/wp-content/uploads/2019/08/Sunny-Leone2.jpg");
        urls.add("https://www.india.com/wp-content/uploads/2016/12/16-7.jpg");
        urls.add("https://static.india.com/wp-content/uploads/2020/02/Sunny-Leone-5.jpg?impolicy=Medium_Resize&w=1200&h=800");
        urls.add("https://static.india.com/wp-content/uploads/2020/02/Sunny-Leone-5.jpg?impolicy=Medium_Resize&w=1200&h=800");
//        urls.add("https://static.india.com/wp-content/uploads/2020/02/Sunny-Leone-5.jpg?impolicy=Medium_Resize&w=1200&h=800");
//        urls.add("https://static.india.com/wp-content/uploads/2020/02/Sunny-Leone-5.jpg?impolicy=Medium_Resize&w=1200&h=800");
//        urls.add("https://static.india.com/wp-content/uploads/2020/02/Sunny-Leone-5.jpg?impolicy=Medium_Resize&w=1200&h=800");
        message.setUrls(urls);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    @Override
    public void onSwipeToReply(Message message) {
        Toast.makeText(getApplicationContext(),message.getMessage(),Toast.LENGTH_LONG).show();
    }
}