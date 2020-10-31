package com.nobodyknows.chatkitlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nobodyknows.chatlayoutview.ChatLayoutView;
import com.nobodyknows.chatmessageview.CONSTANT.MessagePosition;
import com.nobodyknows.chatmessageview.Model.Message;
import com.nobodyknows.chatmessageview.Model.MessageConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int i=0;
    ArrayList<String> ids = new ArrayList<>(Arrays.asList("7014550298","8442000360"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChatLayoutView chatLayoutView = findViewById(R.id.chatlayout_view);
        chatLayoutView.setIds("ROOM1","7014550298");
        Button button = findViewById(R.id.clickme);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatLayoutView.addMessage(getMessages());
            }
        });
    }

    private Message getMessages() {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(new Random().nextInt(999999) +"");
        message.setMessage(getResources().getString(R.string.dummy_text));
        message.setSender(random);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }
}