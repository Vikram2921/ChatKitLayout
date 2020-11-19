package com.nobodyknows.chatkitlayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nobodyknows.chatuserlistview.ChatUserListView;
import com.nobodyknows.chatuserlistview.Listeners.ChatUserListViewListener;
import com.nobodyknows.chatuserlistview.Model.User;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;

import java.util.Date;
import java.util.GregorianCalendar;

public class chatlist extends AppCompatActivity {

    private ChatUserListView chatUserListView;
    int i=0,j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        chatUserListView = findViewById(R.id.chatuserlistview);
        chatUserListView.initialize(this, new ChatUserListViewListener() {
            @Override
            public void onUserSelect(User user) {
                int index = user.getLastMessageStatus().ordinal();
                index++;
                if(index <=4) {
                    user.setLastMessageStatus(MessageStatus.values()[index]);
                }
                user.setUnreadMessageCount(user.getUnreadMessageCount() -1);
                user.setLastMessageDate(new Date());
                chatUserListView.updateLastMessage(user);
            }

            @Override
            public void onClickAudioCall(User user) {

            }

            @Override
            public void onClickVideoCall(User user) {

            }

            @Override
            public void onClickInfoButton(User user) {

            }
        });
        addUser("https://image.winudf.com/v2/image/Y29tLmRlc2l3YWxscGFwZXJzLlN1bm55TGVvbmVfc2NyZWVuXzRfMTUyOTE2MjM1OV8wNDA/screen-4.jpg?fakeurl=1&type=.jpg");
        addUser("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg");
        addUser("https://c8.alamy.com/comp/W59HGN/new-delhi-india-26th-july-2019-bollywood-actress-warina-hussain-during-india-couture-week-2019-credit-jyoti-kapoorpacific-pressalamy-live-news-W59HGN.jpg");
        addUser("https://images.all-free-download.com/images/wallpapers_thum/taapsee_pannu_indian_actress_14451.jpg");
        addUser("https://i.pinimg.com/originals/51/fe/18/51fe180cc453fccbf05652ad051b4803.jpg");
        addUser("https://i.pinimg.com/236x/cb/f0/03/cbf00364ab620296728d680aa25a74ff.jpg");
        addUser("https://www.goodmorningimagesdownload.com/wp-content/uploads/2020/01/Bollywood-Actress-Images-96.jpg");
        addUser("https://lh3.googleusercontent.com/proxy/Q9F4vLRwkc_MSgJeAG3-p3UGfoD89xaYnjiGq9YSKoCf3KYwJiCC2uRNqcaYcwrXulbDtUS2zuj18N2l0i0PRICax_TeGFUPLqZQaVa1nuQOFK80DmCdOyS0Bi7pZDz3xMxNoygvyrwitWh6iblQjPBUfatFQ-W9aAIZMiW6oDFwjqgS5JvOIr0uFdWEjv3uFT1rzMN6THi69sADCeetlSlAMT6Njg");
        addUser("https://www.lifewingz.com/wp-content/uploads/2020/03/rakul_6a.jpg");
        addUser("https://cdnaws.sharechat.com/b830d2ab-5b52-4ae5-9cfc-31ff35d825e8-07c821b7-5784-4d69-b49b-4b262217bdd1_compressed_40.jpg");
        addUser("https://static-koimoi.akamaized.net/wp-content/new-galleries/2016/05/Shraddha-kapoor-f.jpg");
        addUser("https://i.pinimg.com/736x/06/b3/a4/06b3a47be6911706c5e36a53cbfb0bbb.jpg");
        addUser("https://image.winudf.com/v2/image/Y29tLmRlc2l3YWxscGFwZXJzLlN1bm55TGVvbmVfc2NyZWVuXzRfMTUyOTE2MjM1OV8wNDA/screen-4.jpg?fakeurl=1&type=.jpg");
        addUser("https://i.pinimg.com/originals/ed/8d/2a/ed8d2ab996f7c0b137e1b297c2c88333.jpg");
        addUser("https://c8.alamy.com/comp/W59HGN/new-delhi-india-26th-july-2019-bollywood-actress-warina-hussain-during-india-couture-week-2019-credit-jyoti-kapoorpacific-pressalamy-live-news-W59HGN.jpg");
        addUser("https://images.all-free-download.com/images/wallpapers_thum/taapsee_pannu_indian_actress_14451.jpg");
        addUser("https://i.pinimg.com/originals/51/fe/18/51fe180cc453fccbf05652ad051b4803.jpg");
        addUser("https://i.pinimg.com/236x/cb/f0/03/cbf00364ab620296728d680aa25a74ff.jpg");
        addUser("https://www.goodmorningimagesdownload.com/wp-content/uploads/2020/01/Bollywood-Actress-Images-96.jpg");
        addUser("https://lh3.googleusercontent.com/proxy/Q9F4vLRwkc_MSgJeAG3-p3UGfoD89xaYnjiGq9YSKoCf3KYwJiCC2uRNqcaYcwrXulbDtUS2zuj18N2l0i0PRICax_TeGFUPLqZQaVa1nuQOFK80DmCdOyS0Bi7pZDz3xMxNoygvyrwitWh6iblQjPBUfatFQ-W9aAIZMiW6oDFwjqgS5JvOIr0uFdWEjv3uFT1rzMN6THi69sADCeetlSlAMT6Njg");
        addUser("https://www.lifewingz.com/wp-content/uploads/2020/03/rakul_6a.jpg");
        addUser("https://cdnaws.sharechat.com/b830d2ab-5b52-4ae5-9cfc-31ff35d825e8-07c821b7-5784-4d69-b49b-4b262217bdd1_compressed_40.jpg");
        addUser("https://static-koimoi.akamaized.net/wp-content/new-galleries/2016/05/Shraddha-kapoor-f.jpg");
        addUser("https://i.pinimg.com/736x/06/b3/a4/06b3a47be6911706c5e36a53cbfb0bbb.jpg");

        chatUserListView.setMyId("7014550298");
    }

    private void addUser(String url) {
        User user = new User();
        i++;
        j++;
        if(j >= 11) {
            j=0;
        }
        user.setIsGroup(true);
        user.setLastMessageType(MessageType.values()[j]);
        user.setLastMessageStatus(MessageStatus.SENDING);
        user.setUnreadMessageCount(randBetween(-10,40));
        user.setLastMessage("Hello are you busy i want to to meet you can you meet me at that place today at 10:30");
        user.setLastMessageDate(getRandomeDate());
        user.setLastMessageSender("7014550298");
        user.setUserId("8442000360"+i);
        user.setName("Actress # "+(i));
        user.setProfileUrl(url);
        chatUserListView.addUser(user);
    }

    private Date getRandomeDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(2018, 2020);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        int month = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
        gc.set(gc.DAY_OF_MONTH, month);
        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}