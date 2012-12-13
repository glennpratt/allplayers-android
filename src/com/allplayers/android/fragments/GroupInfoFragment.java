package com.allplayers.android.fragments;

import com.allplayers.android.Globals;
import com.allplayers.android.R;
import com.allplayers.objects.GroupData;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupInfoFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GroupData group = Globals.currentGroup;
        String title = group.getTitle();
        String desc = group.getDescription();
        String logoURL = group.getLogo();
        String uuid = group.getUUID();

        getActivity().setTitle(title);


        ImageView imView = (ImageView) getActivity().findViewById(R.id.groupLogo);
        imView.setTag(logoURL);
        new DownloadImagesTask().execute(imView);

        TextView groupInfo = (TextView) getActivity().findViewById(R.id.groupDetails);
        groupInfo.setText("Title: " + title + "\n\nDescription: " + desc);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.groupinfofragment, container, false);
        return v;

    }

    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return Globals.getRemoteImage((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

    }

}
