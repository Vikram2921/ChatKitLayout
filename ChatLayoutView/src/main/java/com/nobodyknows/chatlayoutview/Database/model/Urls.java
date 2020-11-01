package com.nobodyknows.chatlayoutview.Database.model;

import android.util.Log;

public class Urls {
    public static final String TABLE_NAME = "SharedFilesDB";

    private int id;
    private String messageId;
    private String url;
    private String extension;
    private String name;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE_ID = "messageId";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXETENSION = "extension";

    public static final String getTableName(String roomId) {
        return TABLE_NAME+"_"+roomId;
    }

    public static final String getCreateTableQuery(String roomId) {
        String CREATE_TABLE="CREATE TABLE "+getTableName(roomId)+"("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MESSAGE_ID +" TEXT NOT NULL,"
                + COLUMN_URL +" TEXT NOT NULL,"
                + COLUMN_NAME +" TEXT NOT NULL,"
                + COLUMN_EXETENSION +" TEXT NOT NULL"
                +")";
        return CREATE_TABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
