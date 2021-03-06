package com.nobodyknows.chatlistlayoutview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nobodyknows.chatlistlayoutview.Database.model.Chats;
import com.nobodyknows.chatlistlayoutview.Database.model.ContactDbModel;
import com.nobodyknows.chatlistlayoutview.Database.model.Urls;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;
import com.nobodyknows.commonhelper.Model.Contact;
import com.nobodyknows.commonhelper.Model.Message;
import com.nobodyknows.commonhelper.Model.MessageConfiguration;
import com.nobodyknows.commonhelper.Model.SharedFile;
import com.nobodyknows.commonhelper.Services.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NOBODYKNOW_CHATS";
    private String roomId;
    private Helper helper;
    public DatabaseHelper(@Nullable Context context,String roomId) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.roomId = roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public void createTable(String roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Chats.getCreateTableQuery(roomId));
        db.execSQL(Urls.getCreateTableQuery(roomId));
        db.execSQL(ContactDbModel.getCreateTableQuery(roomId));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Chats.getTableName(roomId));
        db.execSQL("DROP TABLE IF EXISTS " + Urls.getTableName(roomId));
        db.execSQL("DROP TABLE IF EXISTS " + ContactDbModel.getTableName(roomId));
        onCreate(db);
    }

    public void delete(SQLiteDatabase db,String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void clear(SQLiteDatabase db,String tableName) {
        db.execSQL("DELETE FROM " + tableName);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        delete(db,Chats.getTableName(roomId));
        delete(db,Urls.getTableName(roomId));
        delete(db,ContactDbModel.getTableName(roomId));
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        clear(db,Chats.getTableName(roomId));
        clear(db,Urls.getTableName(roomId));
        clear(db,ContactDbModel.getTableName(roomId));
    }

    public long insertMessage(Message message) {
        SQLiteDatabase db  = this.getWritableDatabase();
        long id = 0;
        if(!isMessageExist(message.getMessageId(),db)) {
            id = db.insert(Chats.getTableName(roomId),null,getContentValues(message));
            if(message.getSharedFiles().size() > 0) {
                for(SharedFile sharedFile:message.getSharedFiles()) {
                    insertSharedFile(message.getMessageId(),sharedFile);
                }
            }

            if(message.getMessageType() == MessageType.CONTACT) {
                for(Contact contact:message.getContacts()) {
                    insertContact(contact,message);
                }
            }
        }
        db.close();
        return id;
    }

    public long insertSharedFile(String messageId,SharedFile sharedFile) {
        SQLiteDatabase db  = this.getWritableDatabase();
        long id = 0;
        if(!isSharedFileExist(messageId,sharedFile.getName(),db)) {
            id = db.insert(Urls.getTableName(roomId),null,getSharedFileContentValues(messageId,sharedFile));
        }
        db.close();
        return id;
    }

    public long insertContact(Contact contact,Message message) {
        SQLiteDatabase db  = this.getWritableDatabase();
        long id = 0;
        id = db.insert(ContactDbModel.getTableName(roomId),null,getContactContentValues(message,contact));
        db.close();
        return id;
    }

    private ContentValues getContactContentValues(Message message, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactDbModel.COLUMN_MESSAGE_ID,message.getMessageId());
        values.put(ContactDbModel.COLUMN_NAME,contact.getName());
        values.put(ContactDbModel.COLUMN_NUMBERS,contact.getContactNumbers());
        return values;
    }

    private ContentValues getSharedFileContentValues(String messageId, SharedFile sharedFile) {
        ContentValues values = new ContentValues();
        values.put(Urls.COLUMN_MESSAGE_ID,messageId);
        values.put(Urls.COLUMN_FILE_ID,sharedFile.getFileId());
        values.put(Urls.COLUMN_NAME,sharedFile.getName());
        values.put(Urls.COLUMN_URL,sharedFile.getUrl());
        values.put(Urls.COLUMN_LOCAL_PATH,sharedFile.getLocalPath());
        values.put(Urls.COLUMN_FILE_INFO,sharedFile.getFileInfo());
        values.put(Urls.COLUMN_PREVIEW_URL,sharedFile.getPreviewUrl());
        values.put(Urls.COLUMN_EXETENSION,sharedFile.getExtension());
        values.put(Urls.COLUMN_SIZE,sharedFile.getSize());
        values.put(Urls.COLUMN_DURATION,sharedFile.getDuration());
        return values;
    }

    private ContentValues getContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(Chats.COLUMN_MESSAGE_ID,message.getMessageId());
        values.put(Chats.COLUMN_REPLY_MESSAGE_ID,message.getRepliedMessageId());
        values.put(Chats.COLUMN_SENDER,message.getSender());
        values.put(Chats.COLUMN_RECEIVER,message.getReceiver());
        values.put(Chats.COLUMN_ROOM_ID,message.getRoomId());
        values.put(Chats.COLUMN_MESSAGE,message.getMessage());
        values.put(Chats.COLUMN_MESSAGE_TYPE,message.getMessageType().ordinal());
        values.put(Chats.COLUMN_MESSAGE_STATUS,message.getMessageStatus().ordinal());
        values.put(Chats.COLUMN_CREATED_TIME,getConvertedDate(message.getCreatedTimestamp()));
        values.put(Chats.COLUMN_UPDATED_TIME,getConvertedDate(message.getUpdateTimestamp()));
        values.put(Chats.COLUMN_SENTAT,getConvertedDate(message.getSentAt()));
        values.put(Chats.COLUMN_RECEIVEAT,getConvertedDate(message.getReceivedAt()));
        values.put(Chats.COLUMN_SEENAT,getConvertedDate(message.getSeenAt()));
        values.put(Chats.COLUMN_IS_REPLY_MESSAGE,getBooleanToIntValue(message.getIsRepliedMessage()));
        return values;
    }

    public ArrayList<Message> getAllMessages(String myId, MessageConfiguration leftMessageConfiguration, MessageConfiguration rightMessageConfiguration,ArrayList<String> dates) {
        ArrayList<Message> messages = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Chats.getTableName(roomId) + " ORDER BY " +
                Chats.COLUMN_CREATED_TIME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                checkForDate(convertToMessage(cursor,myId,leftMessageConfiguration,rightMessageConfiguration),messages,dates,-1);
            } while (cursor.moveToNext());
        }
        db.close();
        return messages;
    }

    public Message getMessage(String messageId) {
        Message message = null;
        String selectQuery = "SELECT  * FROM " + Chats.getTableName(roomId) + " WHERE " +
                Chats.COLUMN_MESSAGE_ID + " = '"+messageId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                message = convertToMessage(cursor);
            } while (cursor.moveToNext());
        }
        db.close();
        return message;
    }

    public ArrayList<SharedFile> getSharedFiles(String messageId) {
        ArrayList<SharedFile> files = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Urls.getTableName(roomId) + " WHERE " +
                Urls.COLUMN_MESSAGE_ID + " = '"+messageId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                files.add(convertToSharedFile(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        return files;
    }

    public ArrayList<Contact> getContacts(String messageId) {
        ArrayList<Contact> files = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ContactDbModel.getTableName(roomId) + " WHERE " +
                ContactDbModel.COLUMN_MESSAGE_ID + " = '"+messageId+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                files.add(convertToContact(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        return files;
    }

    private SharedFile convertToSharedFile(Cursor cursor) {
        SharedFile file = new SharedFile();
        file.setPreviewUrl(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_PREVIEW_URL)));
        file.setFileId(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_FILE_ID)));
        file.setUrl(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_URL)));
        file.setLocalPath(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_LOCAL_PATH)));
        file.setName(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_NAME)));
        file.setFileInfo(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_FILE_INFO)));
        file.setExtension(cursor.getString(cursor.getColumnIndex(Urls.COLUMN_EXETENSION)));
        file.setSize(cursor.getDouble(cursor.getColumnIndex(Urls.COLUMN_SIZE)));
        file.setDuration(cursor.getDouble(cursor.getColumnIndex(Urls.COLUMN_DURATION)));
        return file;
    }

    private void checkForDate(Message message,ArrayList<Message> messages,ArrayList<String> dates,int index) {
        String formattedText = getFormattedDate(message.getCreatedTimestamp());
        if(!dates.contains(formattedText)) {
            dates.add(formattedText);
            Message dateMessage = new Message();
            dateMessage.setMessageType(MessageType.DATE);
            if(message.getCreatedTimestamp() == new Date()) {
                formattedText = "Today";
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,-1);
                if(message.getCreatedTimestamp() == calendar.getTime()) {
                    formattedText = "Yesterday";
                }
            }
            dateMessage.setMessage(formattedText+"");
            dateMessage.setMessageId("DATE_"+message.getCreatedTimestamp());
            if(index == -1) {
                messages.add(dateMessage);
                helper.addMessageId("DATE_"+message.getCreatedTimestamp());
            } else{
                messages.add(index,dateMessage);
                helper.addMessageId(index,"DATE_"+message.getCreatedTimestamp());
            }
        }
        if(message.getIsRepliedMessage()) {
            if(message.getReplyMessageView() == null) {
                Message replyMessage = messages.get(helper.getMessageIdPositon(message.getRepliedMessageId()));
                message.setReplyMessageView(helper.getReplyMessageView(replyMessage));
            }
        }
        if(index == -1) {
            messages.add(message);
            helper.addMessageId(message.getMessageId());
        } else{
            messages.add(index,message);
            helper.addMessageId(index,message.getMessageId());
        }
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return simpleDateFormat.format(date);
    }

    public ArrayList<Message> getLimitedMessages(String myId, MessageConfiguration leftMessageConfiguration, MessageConfiguration rightMessageConfiguration, ArrayList<String> dates, int limit, int offset) {
        ArrayList<Message> messages = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Chats.getTableName(roomId) + " ORDER BY " +
                Chats.COLUMN_CREATED_TIME + " DESC LIMIT "+limit+" OFFSET "+offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                checkForDate(convertToMessage(cursor,myId,leftMessageConfiguration,rightMessageConfiguration),messages,dates,0);
            } while (cursor.moveToNext());
        }
        db.close();
        return messages;
    }

    private Boolean isMessageExist(String messageId,SQLiteDatabase db) {
        String selectQuery = "SELECT  * FROM " + Chats.getTableName(roomId) + " WHERE " +
                Chats.COLUMN_MESSAGE_ID + " = '"+messageId+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <=0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private Boolean isSharedFileExist(String messageId,String name,SQLiteDatabase db) {
        String selectQuery = "SELECT  * FROM " + Urls.getTableName(roomId) + " WHERE " +
                Urls.COLUMN_MESSAGE_ID + " = "+messageId+" AND "+Urls.COLUMN_NAME+" = '"+name+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <=0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private Message convertToMessage(Cursor cursor, String myId, MessageConfiguration leftMessageConfiguration, MessageConfiguration rightMessageConfiguration) {
        Message message = convertToMessage(cursor);
        if(myId.equals(message.getSender())) {
            message.setMessageConfiguration(rightMessageConfiguration);
        } else {
            message.setMessageConfiguration(leftMessageConfiguration);
        }
        message.setSharedFiles(getSharedFiles(message.getMessageId()));
        if(message.getMessageType() == MessageType.CONTACT) {
            message.setContacts(getContacts(message.getMessageId()));
        }
        return message;
    }

    private Message convertToMessage(Cursor cursor) {
        Message message = new Message();
        message.setMessageId(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_MESSAGE_ID)));
        message.setRepliedMessageId(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_REPLY_MESSAGE_ID)));
        message.setSender(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_SENDER)));
        message.setReceiver(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_RECEIVER)));
        message.setMessage(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_MESSAGE)));
        message.setRoomId(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_ROOM_ID)));
        message.setMessageType(MessageType.values()[cursor.getInt(cursor.getColumnIndex(Chats.COLUMN_MESSAGE_TYPE))]);
        message.setMessageStatus(MessageStatus.values()[cursor.getInt(cursor.getColumnIndex(Chats.COLUMN_MESSAGE_STATUS))]);
        message.setCreatedTimestamp(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_CREATED_TIME))));
        message.setUpdateTimestamp(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_UPDATED_TIME))));
        message.setSentAt(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_SENTAT))));
        message.setReceivedAt(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_RECEIVEAT))));
        message.setSeenAt(getReveretdDate(cursor.getString(cursor.getColumnIndex(Chats.COLUMN_SEENAT))));
        message.setIsRepliedMessage(getBooleanValue(cursor.getInt(cursor.getColumnIndex(Chats.COLUMN_IS_REPLY_MESSAGE))));
        return message;
    }

    private Contact convertToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setName(cursor.getString(cursor.getColumnIndex(ContactDbModel.COLUMN_NAME)));
        contact.setContactNumbers(cursor.getString(cursor.getColumnIndex(ContactDbModel.COLUMN_NUMBERS)));
        return contact;
    }


    private boolean getBooleanValue(int value) {
        if(value >= 1) {
            return true;
        }
        return false;
    }

    private int getBooleanToIntValue(Boolean value) {
        if(value) {
            return 1;
        }
        return 0;
    }

    private void readSharedFiles(String messageId) {

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
        if(date != null) {
            return date.toString();
        }
        return "";
    }

    private Date getReveretdDate(String date) {
        if(date.length() > 0) {
            Date newdate = new Date(date);
            return newdate;
        }
        return null;
    }

    public int updateMessageStatus(String messageId, MessageStatus messageStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Chats.COLUMN_MESSAGE_STATUS, messageStatus.ordinal());
        return db.update(Chats.getTableName(roomId), values, Chats.COLUMN_MESSAGE_ID + " = ?", new String[]{messageId});
    }
}
