package com.nobodyknows.chatlistlayoutview.Database.model;

public class ContactDbModel {
    public static final String TABLE_NAME = "ContactsDB";

    private int id;
    private String messageId;
    private String name;
    private String numbers;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE_ID = "messageId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBERS = "numbers";

    public static final String getTableName(String roomId) {
        return TABLE_NAME+"_"+roomId;
    }

    public static final String getCreateTableQuery(String roomId) {
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+getTableName(roomId)+"("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MESSAGE_ID +" TEXT NOT NULL,"
                + COLUMN_NAME +" TEXT NOT NULL,"
                + COLUMN_NUMBERS +" TEXT"
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

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
