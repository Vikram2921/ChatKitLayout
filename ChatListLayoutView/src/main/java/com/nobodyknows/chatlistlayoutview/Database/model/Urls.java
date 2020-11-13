package com.nobodyknows.chatlistlayoutview.Database.model;

public class Urls {
    public static final String TABLE_NAME = "SharedFilesDB";

    private int id;
    private String messageId;
    private String url;
    private String previewUrl;
    private String fileId;
    private String extension;
    private String fileInfo;
    private String name;
    private Double size;
    private Double duration;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FILE_ID = "fileId";
    public static final String COLUMN_MESSAGE_ID = "messageId";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_PREVIEW_URL = "previewUrl";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FILE_INFO = "fileInfo";
    public static final String COLUMN_EXETENSION = "extension";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_DURATION = "duration";

    public static final String getTableName(String roomId) {
        return TABLE_NAME+"_"+roomId;
    }

    public static final String getCreateTableQuery(String roomId) {
        String CREATE_TABLE="CREATE TABLE "+getTableName(roomId)+"("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FILE_ID +" TEXT NOT NULL,"
                + COLUMN_MESSAGE_ID +" TEXT NOT NULL,"
                + COLUMN_URL +" TEXT NOT NULL,"
                + COLUMN_PREVIEW_URL +" TEXT,"
                + COLUMN_NAME +" TEXT NOT NULL,"
                + COLUMN_FILE_INFO +" TEXT,"
                + COLUMN_EXETENSION +" TEXT NOT NULL,"
                + COLUMN_SIZE +" REAL,"
                + COLUMN_DURATION +" REAL"
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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }
}
