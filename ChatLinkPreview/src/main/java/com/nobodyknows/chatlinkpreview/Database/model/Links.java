package com.nobodyknows.chatlinkpreview.Database.model;

public class Links {
    public static final String TABLE_NAME = "links";

    private int id;
    private String url;
    private String siteName;
    private String description;
    private String imageUrl;
    private String favIcon;
    private String title;
    private String mediaType;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_FAVICON = "favIcon";
    public static final String COLUMN_SITENAME = "siteName";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGEURL = "imageUrl";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MEDIATYPE = "mediaType";


    public static final String getTableName() {
        return TABLE_NAME;
    }

    public static final String getCreateTableQuery() {
        String CREATE_TABLE="CREATE TABLE "+getTableName()+"("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_URL +" TEXT NOT NULL UNIQUE,"
                + COLUMN_SITENAME +" TEXT,"
                + COLUMN_DESCRIPTION +" TEXT,"
                + COLUMN_FAVICON +" TEXT,"
                + COLUMN_IMAGEURL +" TEXT,"
                + COLUMN_TITLE +" TEXT,"
                + COLUMN_MEDIATYPE +" TEXT"
                +")";
        return CREATE_TABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFavIcon() {
        return favIcon;
    }

    public void setFavIcon(String favIcon) {
        this.favIcon = favIcon;
    }
}
