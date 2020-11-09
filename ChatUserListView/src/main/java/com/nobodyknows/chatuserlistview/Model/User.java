package com.nobodyknows.chatuserlistview.Model;

import com.nobodyknows.chatuserlistview.MessageStatus;

import java.util.Date;

public class User {
    private String userId;
    private String name;
    private String profileUrl;
    private String lastMessage;
    private String lastMessageSender;
    private Date lastMessageDate;
    private MessageStatus lastMessageStatus = MessageStatus.SENDING;
    private boolean isGroup = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSender() {
        return lastMessageSender;
    }

    public void setLastMessageSender(String lastMessageSender) {
        this.lastMessageSender = lastMessageSender;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public MessageStatus getLastMessageStatus() {
        return lastMessageStatus;
    }

    public void setLastMessageStatus(MessageStatus lastMessageStatus) {
        this.lastMessageStatus = lastMessageStatus;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean group) {
        isGroup = group;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
