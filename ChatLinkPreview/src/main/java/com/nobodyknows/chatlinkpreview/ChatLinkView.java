package com.nobodyknows.chatlinkpreview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.nobodyknows.chatlinkpreview.Database.ChatLinkDatabaseHelper;

/**
 * Created by ponna on 16-01-2018.
 */

public class ChatLinkView extends ConstraintLayout {

    private View view;
    private Context context;
    private ImageView imageView;
    private ConstraintLayout root;
    private TextView textViewTitle;
    private TextView textViewDesp;
    private TextView textViewUrl;
    private ChatLinkDatabaseHelper chatLinkDatabaseHelper;
    private boolean useDatabase = false;


    public ChatLinkView(Context context) {
        super(context);
        this.context = context;
        init(null,0);
    }

    public ChatLinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs,0);
    }

    public ChatLinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.link_layout,this,true);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.ChatLinkView);
        useDatabase= typedArray.getBoolean(R.styleable.ChatLinkView_useDatabase,false);
        imageView = findViewById(R.id.rich_link_image);
        root = view.findViewById(R.id.rich_link_card);
        textViewTitle = findViewById(R.id.rich_link_title);
        textViewDesp = findViewById(R.id.rich_link_desp);
        textViewUrl = findViewById(R.id.rich_link_url);
    }

    public void setFromLink(String url,ChatViewListener chatViewListener) {
        if(useDatabase) {
            if(chatLinkDatabaseHelper.isLinkExist(url)) {
                MetaData metaData = chatLinkDatabaseHelper.getLinkMetadata(url);
                setupView(metaData);
                chatViewListener.onSuccess(metaData);
            } else {
                setLink(url,chatViewListener);
            }
        } else {
            setLink(url,chatViewListener);
        }
    }

    public void setFromLink(String url) {
        if(useDatabase) {
            if(chatLinkDatabaseHelper.isLinkExist(url)) {
                MetaData metaData = chatLinkDatabaseHelper.getLinkMetadata(url);
                setupView(metaData);
            } else {
                setLink(url);
            }
        } else {
            setLink(url);
        }
    }

    private void setupView(MetaData metaData) {
        root.setVisibility(VISIBLE);
        if(metaData.getImageurl().equals("") || metaData.getImageurl().isEmpty()) {
            imageView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
            Glide.with(getContext())
                    .load(metaData.getImageurl())
                    .override(100)
                    .into(imageView);
        }

        if(metaData.getTitle().isEmpty() || metaData.getTitle().equals("")) {
            textViewTitle.setVisibility(GONE);
        } else {
            textViewTitle.setVisibility(VISIBLE);
            textViewTitle.setText(metaData.getTitle());
        }
        if(metaData.getUrl().isEmpty() || metaData.getUrl().equals("")) {
            textViewUrl.setVisibility(GONE);
        } else {
            textViewUrl.setVisibility(VISIBLE);
            textViewUrl.setText(metaData.getUrl());
        }
        if(metaData.getDescription().isEmpty() || metaData.getDescription().equals("")) {
            textViewDesp.setVisibility(GONE);
        } else {
            textViewDesp.setVisibility(VISIBLE);
            textViewDesp.setText(metaData.getDescription());
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(metaData.getUrl()));
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setFromMetadata(MetaData metaData) {
        setupView(metaData);
    }

    private void setLink(String url, final ChatViewListener chatViewListener) {
        Log.d("TAGURL", "setLink: "+url);
        LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                if(!metaData.getTitle().isEmpty() || !metaData.getTitle().equals("")) {
                    setupView(metaData);
                    metaData.setUrl(url);
                    if(useDatabase) {
                        chatLinkDatabaseHelper.insertLink(metaData);
                    }
                    chatViewListener.onSuccess(metaData);
                }
            }

            @Override
            public void onError(Exception e) {
                chatViewListener.onError(e);
            }
        });
        linkPreview.getPreview(url);
    }

    private void setLink(String url) {
        LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                if(!metaData.getTitle().isEmpty() || !metaData.getTitle().equals("")) {
                    metaData.setUrl(url);
                    if(useDatabase) {
                        chatLinkDatabaseHelper.insertLink(metaData);
                    }
                    setupView(metaData);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
        linkPreview.getPreview(url);
    }


    public ChatLinkDatabaseHelper getChatLinkDatabaseHelper() {
        return chatLinkDatabaseHelper;
    }

    public void setChatLinkDatabaseHelper(ChatLinkDatabaseHelper chatLinkDatabaseHelper) {
        this.chatLinkDatabaseHelper = chatLinkDatabaseHelper;
    }
}
