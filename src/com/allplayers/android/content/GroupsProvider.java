package com.allplayers.android.content;

import java.io.IOException;
import java.net.ContentHandler;

import com.allplayers.android.net.IndexContentHandler;
import com.google.android.feeds.FeedLoader;
import com.google.android.feeds.FeedProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;

public class GroupsProvider extends ContentProvider {

    Context mContext;

    @Override
    public boolean onCreate() {
        Context mContext = getContext();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO - Register Uri
        // content://com.google.android.demos.jamendo/artists?n=20
        // TODO - What to do with projection?
        // Jamendo... [_id, artist_image, artist_name, artist_genre]

        MatrixCursor output = new MatrixCursor(projection);

        // TODO - Finish this out to populate the Cursor.
        ContentHandler handler = new IndexContentHandler(output);

        // TODO - Get URI from network class.
        Uri base = Uri.parse("http://www.allplayers.com/");

        // TODO - Handle incoming URI.
        Uri.Builder builder = base.buildUpon();
        builder.appendPath("api/vi/rest");

        // TODO - Store UUID on login / with account.
        String uuid = "2531d044-f611-11e0-a44b-12313d04fc0f";
        builder.appendPath("users/" + uuid + "/groups");

        Bundle extras = new Bundle();

        try {
            // This isn't going to work because loadDocument is static and doesn't include authentication.
            // DIY...
            FeedLoader.loadPagedFeed(handler, builder.build(), "page", 0, 20, 20, extras);
            return FeedProvider.feedCursor(output, extras);
        } catch (IOException e) {
            // TODO - solution could be valuable to pass Intent for account.
            return FeedProvider.errorCursor(output, extras, e, null);
        }
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
