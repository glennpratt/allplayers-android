package com.allplayers.android;
 
import com.allplayers.objects.AlbumData;
import com.allplayers.rest.RestApiV1;
 
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
import java.util.ArrayList;
import java.util.List;
 
import org.json.JSONException;
import org.json.JSONObject;
 
public class AlbumAdapter extends ArrayAdapter<AlbumData> {
    private ImageView albumCoverPhoto;
    private TextView albumTitle;
    private TextView albumExtraInfo;
    private List<AlbumData> albums = new ArrayList<AlbumData>();
 
    public AlbumAdapter(Context context, int textViewResourceId, List<AlbumData> objects) {
        super(context, textViewResourceId, objects);
        this.albums = objects;
    }
 
    public int getCount() {
        return albums.size();
    }
 
    public AlbumData getItem(int index) {
        return albums.get(index);
    }
 
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
        albumCoverPhoto = (ImageView)row.findViewById(R.id.albumCoverPhoto);
 
        //Get reference to TextView - albumTitle
        albumTitle = (TextView)row.findViewById(R.id.albumTitle);
 
        //Get reference to TextView - albumExtraInfo
        albumExtraInfo = (TextView)row.findViewById(R.id.albumExtraInfo);
 
        //Set album title
        albumTitle.setText(album.getTitle());
 
        //Set cover photo icon
        String imageURL = album.getCoverPhoto();
 
        if (!imageURL.trim().equals("")) {
            CoverPhotoTask helper = new CoverPhotoTask();
        	  helper.execute(album);
        }
 
        //Set extra info
        albumExtraInfo.setText("");
        return row;
    }
    public class CoverPhotoTask extends AsyncTask<AlbumData, Void, Bitmap> {
    	 
        protected Bitmap doInBackground(AlbumData... albums) {
        	return RestApiV1.getRemoteImage(albums[0].getCoverPhoto());
        }
         
 		protected void onPostExecute(Bitmap bitmap) {
 			albumCoverPhoto.setImageBitmap(bitmap);
     	}
     }
}