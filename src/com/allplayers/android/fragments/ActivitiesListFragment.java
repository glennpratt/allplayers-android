package com.allplayers.android.fragments;

import com.allplayers.android.EventsActivity;
import com.allplayers.android.GroupsActivity;
import com.allplayers.android.MessageActivity;
import com.allplayers.android.PhotosActivity;
import com.allplayers.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivitiesListFragment extends ListFragment {
    static final String[] ACTIVITIES = new String[] {
        "Groups", "Messages", "Photos", "Events"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, ACTIVITIES));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String activity = (String) ((TextView) v).getText();
        if (activity == "Groups") {
            getActivity().startActivity(new Intent(getActivity(), GroupsActivity.class));
        }
        else if (activity == "Messages") {
            getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
        }
        else if (activity == "Photos") {
            getActivity().startActivity(new Intent(getActivity(), PhotosActivity.class));
        }
        else if (activity == "Events") {
            getActivity().startActivity(new Intent(getActivity(), EventsActivity.class));
        }

    }
}
