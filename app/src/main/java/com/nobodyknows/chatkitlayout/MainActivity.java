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
import com.nobodyknows.chatlayoutview.Model.Contact;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.MessageConfiguration;
import com.nobodyknows.chatlayoutview.Model.SharedFile;
import com.nobodyknows.chatlayoutview.Model.User;
import com.nobodyknows.chatlayoutview.Services.UploadAndDownloadView;
import com.nobodyknows.chatlayoutview.Services.UploadAndDownloadViewHandler;

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
    View selView;
    ArrayList<String> ids = new ArrayList<>(Arrays.asList("7014550298","8442000360"));
    UploadAndDownloadViewHandler uploadAndDownloadViewHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.viewhold);
        uploadAndDownloadViewHandler = new UploadAndDownloadViewHandler(getApplicationContext());
        ChatLayoutView chatLayoutView = findViewById(R.id.chatlayout_view);
        chatLayoutView.setChatLayoutListener(this);
        chatLayoutView.setUploadAndDownloadViewHandler(uploadAndDownloadViewHandler);
        chatLayoutView.setMainActivityContext(getApplicationContext());
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
        myUserObject.setUserId("7014550298");
        User freindUserObject = new User();
        freindUserObject.setName("Pritam Singh Rathore");
        freindUserObject.setProfileUrl("");
        freindUserObject.setColor(Color.RED);
        freindUserObject.setUserId("8442000360");
        chatLayoutView.addUser(myUserObject);
        chatLayoutView.addUser(freindUserObject);
        chatLayoutView.loadAllDBMessage();

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
//        chatLayoutView.addMessage(getGifMessage("asdasd"));
        chatLayoutView.addMessage(getStickerMessage("https://i.giphy.com/media/9Dk1ba2smFg2KASTcz/200.webp",8));
        chatLayoutView.addMessage(getContactMessage("12345678",false));
        chatLayoutView.addMessage(getAudioMessages("12345678","112312",false,""));
        chatLayoutView.addMessage(getAudioMessages("12345678","112316",false,""));
//        chatLayoutView.addMessage(getImageMessages("Dummy Message",Arrays.asList("https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2F1596103576_K_Advani.jpg?alt=media&token=b7655d77-eb2e-4dff-a348-cd8ae0a365b9")));
//        chatLayoutView.addMessage(getImageMessages("Dummy Message",Arrays.asList("https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2FActress-Kiara-Advani-Gorgeous-new-still-.jpg?alt=media&token=a88e01ac-49bf-4134-8104-934ba1bf79dd","https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2FKatrina-Kaif-in-pink-saree.jpg?alt=media&token=58091660-c0f6-4d76-bae1-1e8f89e35852")));
//        chatLayoutView.addMessage(getImageMessages("Dummy Message",Arrays.asList("https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2FKiara-Advani-Photo.jpg?alt=media&token=6bbf1c1b-0947-4b6b-896b-a09d95f01d68")));
//        chatLayoutView.addMessage(getImageMessages("Dummy Message",Arrays.asList("https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2FKiara_Advani_walks_for_Shyamal-Bhumika_at_India_Couture_Week_2018_Day_4_(03)_(cropped).jpg?alt=media&token=1c284215-bb2e-4e41-9cac-3e3f731a4e2e")));
//        chatLayoutView.addMessage(getImageMessages("Dummy Message",Arrays.asList("https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2Fbollywood-actresses-saree-slips_13620316171.jpg?alt=media&token=38479b5b-7665-43e6-b4ab-c57c793194fb",
//                "https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2Fkiara-advani-1200.jpg?alt=media&token=f5f566ba-e8c0-4d7b-8774-2644cc95739b",
//                "https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2Fkiara_advani_1603192093.webp?alt=media&token=4033f3bf-2cd5-4c2b-99eb-c431eaf2ef78",
//                "https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2Fimages.jfif?alt=media&token=1f379911-ef8c-4476-8125-1c793c17a6d8","https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2Fimages%20(1).jfif?alt=media&token=0f155298-8051-4348-9603-cce1e2e6af28",
//                "https://firebasestorage.googleapis.com/v0/b/chatme-9b152.appspot.com/o/Testing%2Fsaree16.jpg?alt=media&token=a0feb64f-13e3-4704-b884-1f6fbc7dfff9")));
    }


    private Message getContactMessage( String s, boolean b) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(s);
        message.setIsRepliedMessage(b);
        message.setMessageType(MessageType.CONTACT);
        message.setMessage("");
        message.setSender(random);
        Contact contact = new Contact();
        contact.setName("NobodyKnows");
        contact.setContactNumbers("+91 7014550298");
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Vishal Pamble","8290879124,7014560298"));
        message.addContact(getContact("Pritam Singh Rathore","8442000360"));
        message.addContact(getContact("Vikram Singh Rawat","7014550298"));
        message.addContact(contact);
        message.setMessageStatus(MessageStatus.SENT);
        message.setReceiver(random.equals("7014550298")?"8442000360":"7014550298");
        message.setSeenAt(new Date());
        message.setSentAt(new Date());
        message.setReceivedAt(new Date());
        return message;
    }

    private Contact getContact(String name,String number) {
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

    private Message getRecordingMessages(String string, String s, boolean b, String url) {
        String random = ids.get(new Random().nextInt(2));
        Message message = new Message();
        message.setMessageId(s);
        message.setIsRepliedMessage(b);
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
        message.setMessageType(MessageType.IMAGE);
        message.setMessageId("2751300");
        message.setMessage(messageText);
        message.setSender(random);
        message.addSharedFile(getSharedFile("https://media3.giphy.com/media/26ybvqyqoH7UbyHeg/giphy.gif",message.getMessageId()+"5","gif"));
        message.addSharedFile(getSharedFile("https://media4.giphy.com/media/3o6YgaqfCPku7lUSoE/giphy.gif",message.getMessageId()+"6","gif"));
        message.addSharedFile(getSharedFile("https://media4.giphy.com/media/3o6YgaqfCPku7lUSoE/giphy.gif",message.getMessageId()+"6","gif"));
        message.addSharedFile(getSharedFile("https://media4.giphy.com/media/3o6YgaqfCPku7lUSoE/giphy.gif",message.getMessageId()+"6","gif"));
        message.addSharedFile(getSharedFile("https://media4.giphy.com/media/3o6YgaqfCPku7lUSoE/giphy.gif",message.getMessageId()+"6","gif"));
        message.addSharedFile(getSharedFile("https://media4.giphy.com/media/3o6YgaqfCPku7lUSoE/giphy.gif",message.getMessageId()+"6","gif"));
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