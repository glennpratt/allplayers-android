package com.allplayers.android;

import com.allplayers.android.widget.ImageLoader;
import com.allplayers.objects.AlbumData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends ArrayAdapter<AlbumData> {
    private List<AlbumData> albums = new ArrayList<AlbumData>();
    public ImageLoader imageLoader;

    public AlbumAdapter(Context context, int textViewResourceId, List<AlbumData> objects) {
        super(context, textViewResourceId, objects);
        this.albums = objects;
        imageLoader=new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public AlbumData getItem(int index) {
        return albums.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            //Row Inflation
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.albumlistitem, parent, false);
        }

        //Get item
        AlbumData album = getItem(position);

        //Get reference to ImageView
        ImageView albumCoverPhoto = (ImageView)row.findViewById(R.id.albumCoverPhoto);

        //Get reference to TextView - albumTitle
        TextView albumTitle = (TextView)row.findViewById(R.id.albumTitle);

        //Get reference to TextView - albumExtraInfo
        TextView albumExtraInfo = (TextView)row.findViewById(R.id.albumExtraInfo);

        //Set album title
        albumTitle.setText(album.getTitle());

        //Set cover photo icon
        String imageURL = album.getCoverPhoto().trim();

        if (!imageURL.equals("")) {
            imageLoader.DisplayImage(imageURL, albumCoverPhoto);
        }

        //Set extra info
        albumExtraInfo.setText("");
        return row;
    }
}