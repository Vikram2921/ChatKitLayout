package com.nobodyknows.chatlinkpreview;

/**
 * Created by ponna on 16-01-2018.
 */

public interface ChatViewListener {

    void onSuccess(MetaData metaData);

    void onError(Exception e);
}
