package com.nobodyknows.chatlinkpreview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nobodyknows.chatlinkpreview.Database.model.Links;
import com.nobodyknows.chatlinkpreview.MetaData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatLinkDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NOBODYKNOW_LINKS";
    public ChatLinkDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Links.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Links.getTableName());
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
        delete(db,Links.getTableName());
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        clear(db,Links.getTableName());
    }

    public long insertLink(MetaData metaData) {
        SQLiteDatabase db  = this.getWritableDatabase();
        long id = 0;
        if(!isLinkExist(metaData.getUrl(),db)) {
            id = db.insert(Links.getTableName(),null,getLinkValues(metaData));
        }
        db.close();
        return id;
    }


    private ContentValues getLinkValues(MetaData linkInfo) {
        ContentValues values = new ContentValues();
        values.put(Links.COLUMN_URL,linkInfo.getUrl());
        values.put(Links.COLUMN_DESCRIPTION,linkInfo.getDescription());
        values.put(Links.COLUMN_FAVICON,linkInfo.getFavicon());
        values.put(Links.COLUMN_IMAGEURL,linkInfo.getImageurl());
        values.put(Links.COLUMN_MEDIATYPE,linkInfo.getMediatype());
        values.put(Links.COLUMN_SITENAME,linkInfo.getSitename());
        values.put(Links.COLUMN_TITLE,linkInfo.getTitle());
        return values;
    }


    public MetaData getLinkMetadata(String url) {
        MetaData metaData = null;
        String selectQuery = "SELECT  * FROM " + Links.getTableName() + " WHERE " +
                Links.COLUMN_URL + " = '"+url+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                metaData = convertToLinkMetaData(cursor);
            } while (cursor.moveToNext());
        }
        db.close();
        return metaData;
    }


    private Boolean isLinkExist(String url,SQLiteDatabase db) {
        String selectQuery = "SELECT  * FROM " + Links.getTableName() + " WHERE " +
                Links.COLUMN_URL + " = '"+url+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <=0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Boolean isLinkExist(String url) {
        return isLinkExist(url,this.getReadableDatabase());
    }

    private MetaData convertToLinkMetaData(Cursor cursor) {
        MetaData metaData = new MetaData();
        metaData.setUrl(cursor.getString(cursor.getColumnIndex(Links.COLUMN_URL)));
        metaData.setFavicon(cursor.getString(cursor.getColumnIndex(Links.COLUMN_FAVICON)));
        metaData.setSitename(cursor.getString(cursor.getColumnIndex(Links.COLUMN_SITENAME)));
        metaData.setDescription(cursor.getString(cursor.getColumnIndex(Links.COLUMN_DESCRIPTION)));
        metaData.setImageurl(cursor.getString(cursor.getColumnIndex(Links.COLUMN_IMAGEURL)));
        metaData.setTitle(cursor.getString(cursor.getColumnIndex(Links.COLUMN_TITLE)));
        metaData.setMediatype(cursor.getString(cursor.getColumnIndex(Links.COLUMN_MEDIATYPE)));
        return metaData;
    }
}
