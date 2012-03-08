package com.allplayers.android.fragments;

import com.allplayers.android.Globals;
import com.allplayers.android.R;
import com.allplayers.objects.GroupData;

import android.graphics.Bitmap;
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


        Bitmap logo = Globals.getRemoteImage(logoURL);

        ImageView imView = (ImageView) getActivity().findViewById(R.id.groupLogo);
        imView.setImageBitmap(logo);

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

}
