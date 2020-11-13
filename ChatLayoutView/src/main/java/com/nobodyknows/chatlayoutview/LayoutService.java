package com.nobodyknows.chatlayoutview;

import android.text.style.URLSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.nobodyknows.chatlinkpreview.ChatLinkView;
import com.nobodyknows.chatlinkpreview.ChatViewListener;
import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;
import com.nobodyknows.chatlinkpreview.MetaData;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.VISIBLE;

public class LayoutService {
    public static void checkForLink(ChatLinkView chatLinkView, TextView message, ChatLinkDatabaseHelper chatLinkDatabaseHelper) {
        URLSpan span[] = message.getUrls();
        if(span.length > 0) {
            String link = span[0].getURL();
            if(link != null && link.length() > 0) {
                chatLinkView.setChatLinkDatabaseHelper(chatLinkDatabaseHelper);
                chatLinkView.setFromLink(link, new ChatViewListener() {
                    @Override
                    public void onSuccess(MetaData metaData) {
                        chatLinkView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }
    }

    public static String getFormatedDate(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String currentTime = sdf.format(date);
        return currentTime.toUpperCase();
    }

    public static void updateMessageStatus(MessageStatus status, ImageView imageView) {
        if(status == MessageStatus.SEEN) {
            imageView.setImageResource(com.nobodyknows.messageview.R.drawable.seen);
        } else if(status == MessageStatus.RECEIVED) {
            imageView.setImageResource(com.nobodyknows.messageview.R.drawable.received);
        }  else if(status == MessageStatus.SENT) {
            imageView.setImageResource(com.nobodyknows.messageview.R.drawable.sent);
        } else if(status == MessageStatus.SENDING) {
            imageView.setImageResource(com.nobodyknows.messageview.R.drawable.waiting); //TODO Change icon
        }
    }

    public static boolean containsURL(String content){
        String REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        if(m.find()) {
            return true;
        }

        return false;
    }
}
