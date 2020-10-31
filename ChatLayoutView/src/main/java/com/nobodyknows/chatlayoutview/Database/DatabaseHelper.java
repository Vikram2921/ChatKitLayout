package com.nobodyknows.chatlayoutview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nobodyknows.chatlayoutview.Database.model.Chats;
import com.nobodyknows.chatmessageview.CONSTANT.MessageType;
import com.nobodyknows.chatmessageview.Model.Message;
import com.nobodyknows.chatmessageview.Model.MessageConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NOBODYKNOW_CHATS";
    private String roomId;
    public DatabaseHelper(@Nullable Context context,String roomId) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.roomId = roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Chats.getCreateTableQuery(roomId));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Chats.getTableName(roomId));
        onCreate(db);
    }

    public long insertMessage(Message message) {
        SQLiteDatabase db  = this.getWritableDatabase();
        long id = 0;
        if(!isMessageExist(message.getMessageId(),db)) {
            id = db.insert(Chats.getTableName(roomId),null,getContentValues(message));
        }
        db.close();
        return id;
    }

    private ContentValues getContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(Chats.COLUMN_MESSAGE_ID,message.getMessageId());
        values.put(Chats.COLUMN_SENDER,message.getSender());
        values.put(Chats.COLUMN_RECEIVER,message.getReceiver());
        values.put(Chats.COLUMN_ROOM_ID,message.getRoomId());
        values.put(Chats.COLUMN_MESSAGE,message.getMessage());
        values.put(Chats.COLUMN_MESSAGE_TYPE,message.getMessageType().ordinal());
        values.put(Chats.COLUMN_CREATED_TIME,getConvertedDate(message.getCreatedTimestamp()));
        values.put(Chats.COLUMN_UPDATED_TIME,getConvertedDate(message.getUpdateTimestamp()));
        values.put(Chats.COLUMN_SENTAT,getConvertedDate(message.getSentAt()));
        values.put(Chats.COLUMN_RECEIVEAT,getConvertedDate(message.getReceivedAt()));
        values.put(Chats.COLUMN_SEENAT,getConvertedDate(message.getSeenAt()));
        return values;
    }

    public ArrayList<Message> getAllMessages(String myId, MessageConfiguration leftMessageConfiguration, MessageConfiguration rightMessageConfiguration) {
        ArrayList<Message> messages = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Chats.getTableName(roomId) + " ORDER BY " +
                Chats.COLUMN_CREATED_TIME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                messages.add(convertToMessage(cursor,myId,leftMessageConfiguration,rightMessageConfiguration));
            } while (cursor.moveToNext());
        }
        db.close();
        return messages;
    }

    private Boolean isMessageExist(String messageId,SQLiteDatabase db) {
        List<Message> messages = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Chats.getTableName(roomId) + " WHERE " +
                Chats.COLUMN_MESSAGE_ID + " = "+messageId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <=0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private Message convertToMessage(Cursor cursor, String myId, MessageConfiguration leftMessageConfiguration, MessageConfiguration rightMessageConfiguration) {
        Message message = new Message();
        message.setMessageId(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_MESSAGE_ID)));
        message.setSender(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_SENDER)));
        message.setReceiver(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_RECEIVER)));
        message.setMessage(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_MESSAGE)));
        message.setRoomId(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_ROOM_ID)));
        message.setMessageType(MessageType.values()[cursor.getInt(cursor.getColumnIndex(Chats.COLUMN_MESSAGE_TYPE))]);
        message.setCreatedTimestamp(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_CREATED_TIME))));
        message.setUpdateTimestamp(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_UPDATED_TIME))));
        message.setSentAt(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_SENTAT))));
        message.setReceivedAt(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_RECEIVEAT))));
        message.setSeenAt(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_SEENAT))));
        if(myId.equals(message.getSender())) {
            message.setMessageConfiguration(rightMessageConfiguration);
        } else {
            message.setMessageConfiguration(leftMessageConfiguration);
        }
        return message;
    }

    public int getMessagesCount() {
        String countQuery = "SELECT  * FROM " + Chats.getTableName(roomId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    private String getConvertedDate(Date date) {
        return date.toString();
    }

    private Date getReveretdDate(String date) {
        Date newdate = new Date(date);
        return newdate;
    }
}
