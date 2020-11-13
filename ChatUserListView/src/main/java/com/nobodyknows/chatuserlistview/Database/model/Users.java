package com.nobodyknows.chatuserlistview.Database.model;

public class Users {
    public static final String TABLE_NAME = "Users";

    private int id;
    private String name;
    private String userId;
    private String profileUrl;
    private String lastMessage;
    private String lastMessageSender;
    private String lastMessageDate;
    private Integer lastMessageDateForSorting;
    private Integer lastMessageStatus;
    private Integer lastMessageType;
    private Integer isGroup;
    private Integer isBlocked;
    private Integer isPinned;
    private Integer isMuted;
    private Integer unreadCount;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERID = "userId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PROFILEURL = "profileUrl";
    public static final String COLUMN_LASTMESSAGE = "lastMessage";
    public static final String COLUMN_LASTMESSAGESENDER = "lastMessageSender";
    public static final String COLUMN_LASTMESSAGEDATE = "lastMessageDate";
    public static final String COLUMN_LASTMESSAGEDATE_FOR_SORTING = "lastMessageDateForSorting";
    public static final String COLUMN_LASTMESSAGESTATUS = "lastMessageStatus";
    public static final String COLUMN_LASTMESSAGETYPE = "lastMessageType";
    public static final String COLUMN_ISGROUP = "isGroup";
    public static final String COLUMN_ISBLOCKED = "isBlocked";
    public static final String COLUMN_ISPINNED = "isPinned";
    public static final String COLUMN_ISMUTED = "isMuted";
    public static final String COLUMN_UNREADCOUNT = "unreadCount";


    public static final String getTableName() {
        return TABLE_NAME;
    }

    public static final String getCreateTableQuery() {
        String CREATE_TABLE="CREATE TABLE "+getTableName()+"("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERID +" TEXT NOT NULL UNIQUE,"
                + COLUMN_NAME+" TEXT,"
                + COLUMN_PROFILEURL +" TEXT,"
                + COLUMN_LASTMESSAGE +" TEXT,"
                + COLUMN_LASTMESSAGESENDER +" TEXT,"
                + COLUMN_LASTMESSAGEDATE +" TEXT,"
                + COLUMN_LASTMESSAGEDATE_FOR_SORTING +" INTEGER,"
                + COLUMN_LASTMESSAGESTATUS +" INTEGER,"
                + COLUMN_LASTMESSAGETYPE +" INTEGER,"
                + COLUMN_ISGROUP +" INTEGER,"
                + COLUMN_ISBLOCKED +" INTEGER,"
                + COLUMN_ISPINNED +" INTEGER,"
                + COLUMN_UNREADCOUNT +" INTEGER,"
                + COLUMN_ISMUTED +" INTEGER"
                +")";
        return CREATE_TABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public Integer getLastMessageDateForSorting() {
        return lastMessageDateForSorting;
    }

    public void setLastMessageDateForSorting(Integer lastMessageDateForSorting) {
        this.lastMessageDateForSorting = lastMessageDateForSorting;
    }

    public Integer getLastMessageStatus() {
        return lastMessageStatus;
    }

    public void setLastMessageStatus(Integer lastMessageStatus) {
        this.lastMessageStatus = lastMessageStatus;
    }

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer blocked) {
        isBlocked = blocked;
    }

    public Integer getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Integer pinned) {
        isPinned = pinned;
    }

    public Integer getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(Integer isMuted) {
        this.isMuted = isMuted;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Integer getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(Integer lastMessageType) {
        this.lastMessageType = lastMessageType;
    }
}
