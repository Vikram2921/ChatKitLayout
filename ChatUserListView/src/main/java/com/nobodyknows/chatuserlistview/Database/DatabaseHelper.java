package com.nobodyknows.chatuserlistview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nobodyknows.chatuserlistview.Database.model.Users;
import com.nobodyknows.chatuserlistview.Model.User;
import com.nobodyknows.commonhelper.CONSTANT.MessageStatus;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NOBODYKNOW_CHAT_USER_LIST";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Users.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Users.getTableName());
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
        delete(db,Users.getTableName());
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        clear(db,Users.getTableName());
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_LASTMESSAGE, user.getLastMessage());
        values.put(Users.COLUMN_LASTMESSAGE_ID, user.getLastMessageId());
        values.put(Users.COLUMN_LASTMESSAGETYPE,user.getLastMessageType().ordinal());
        values.put(Users.COLUMN_LASTMESSAGEDATE, getConvertedDate(user.getLastMessageDate()));
        values.put(Users.COLUMN_LASTMESSAGEDATE_FOR_SORTING, getFormattedDateForSorting(user.getLastMessageDate()));
        values.put(Users.COLUMN_LASTMESSAGESENDER, user.getLastMessageSender());
        values.put(Users.COLUMN_LASTMESSAGESTATUS, user.getLastMessageStatus().ordinal());
        values.put(Users.COLUMN_UNREADCOUNT, user.getUnreadMessageCount());
        return db.update(Users.getTableName(), values, Users.COLUMN_USERID + " = ?", new String[]{user.getUserId()});
    }

    public long insertUser(User user) {
        SQLiteDatabase db  = this.getWritableDatabase();
        long id = 0;
        if(!isUserExist(user.getUserId(),db)) {
            id = db.insert(Users.getTableName(),null,getContentValues(user));
        }
        db.close();
        return id;
    }


    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_USERID,user.getUserId());
        values.put(Users.COLUMN_NAME,user.getName());
        values.put(Users.COLUMN_PROFILEURL,user.getProfileUrl());
        values.put(Users.COLUMN_LASTMESSAGE,user.getLastMessage());
        values.put(Users.COLUMN_LASTMESSAGE_ID,user.getLastMessageId());
        values.put(Users.COLUMN_LASTMESSAGESENDER,user.getLastMessageSender());
        values.put(Users.COLUMN_LASTMESSAGEDATE,getConvertedDate(user.getLastMessageDate()));
        values.put(Users.COLUMN_LASTMESSAGEDATE_FOR_SORTING,getFormattedDateForSorting(user.getLastMessageDate()));
        values.put(Users.COLUMN_LASTMESSAGESTATUS,user.getLastMessageStatus().ordinal());
        values.put(Users.COLUMN_LASTMESSAGETYPE,user.getLastMessageType().ordinal());
        values.put(Users.COLUMN_ISGROUP,getBooleanToIntValue(user.getIsGroup()));
        values.put(Users.COLUMN_ISBLOCKED,getBooleanToIntValue(user.getIsBlocked()));
        values.put(Users.COLUMN_ISPINNED,getBooleanToIntValue(user.getIsPinned()));
        values.put(Users.COLUMN_ISMUTED,getBooleanToIntValue(user.getIsMuted()));
        values.put(Users.COLUMN_UNREADCOUNT,user.getUnreadMessageCount());
        return values;
    }

    private User convertToUser(Cursor cursor) {
        User user = new User();
        user.setUserId(cursor.getString(cursor.getColumnIndex(Users.COLUMN_USERID)));
        user.setName(cursor.getString(cursor.getColumnIndex(Users.COLUMN_NAME)));
        user.setProfileUrl(cursor.getString(cursor.getColumnIndex(Users.COLUMN_PROFILEURL)));
        user.setLastMessage(cursor.getString(cursor.getColumnIndex(Users.COLUMN_LASTMESSAGE)));
        user.setLastMessageId(cursor.getString(cursor.getColumnIndex(Users.COLUMN_LASTMESSAGE_ID)));
        user.setLastMessageSender(cursor.getString(cursor.getColumnIndex(Users.COLUMN_LASTMESSAGESENDER)));
        user.setLastMessageDate(getReveretdDate(cursor.getString(cursor.getColumnIndex(Users.COLUMN_LASTMESSAGEDATE))));
        user.setLastMessageStatus(MessageStatus.values()[cursor.getInt(cursor.getColumnIndex(Users.COLUMN_LASTMESSAGESTATUS))]);
        user.setLastMessageType(MessageType.values()[cursor.getInt(cursor.getColumnIndex(Users.COLUMN_LASTMESSAGETYPE))]);
        user.setIsGroup(getBooleanValue(cursor.getInt(cursor.getColumnIndex(Users.COLUMN_ISGROUP))));
        user.setIsBlocked(getBooleanValue(cursor.getInt(cursor.getColumnIndex(Users.COLUMN_ISBLOCKED))));
        user.setIsPinned(getBooleanValue(cursor.getInt(cursor.getColumnIndex(Users.COLUMN_ISPINNED))));
        user.setIsMuted(getBooleanValue(cursor.getInt(cursor.getColumnIndex(Users.COLUMN_ISMUTED))));
        user.setUnreadMessageCount(cursor.getInt(cursor.getColumnIndex(Users.COLUMN_UNREADCOUNT)));
        return user;
    }

    private Boolean isUserExist(String userId,SQLiteDatabase db) {
        String selectQuery = "SELECT  * FROM " + Users.getTableName() + " WHERE " +
                Users.COLUMN_USERID + " = "+userId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <=0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
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


    public void getOrderedList(ArrayList<String> usersIds, ArrayList<User> users) {
        String selectQuery = "SELECT  * FROM " + Users.getTableName() + " ORDER BY " + Users.COLUMN_LASTMESSAGEDATE_FOR_SORTING + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = convertToUser(cursor);
                usersIds.add(user.getUserId());
                users.add(user);
            } while (cursor.moveToNext());
        }
        db.close();
    }

    private long getFormattedDateForSorting(Date date) {
        if(date != null) {
            return date.getTime();
        }
        return 0;
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


}
