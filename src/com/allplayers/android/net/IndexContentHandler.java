package com.allplayers.android.net;

import org.json.JSONArray;
import org.json.JSONException;

import android.database.MatrixCursor;

import com.google.android.feeds.FeedLoader;
import com.google.android.feeds.JsonContentHandler;

public class IndexContentHandler extends JsonContentHandler {

    MatrixCursor mOutput;

    public IndexContentHandler(MatrixCursor output) {
        mOutput = output;
    }

    @Override
    protected Object getContent(String source) throws JSONException {
        JSONArray array = new JSONArray(source);

        return FeedLoader.documentInfo(array.length());
    }

}
