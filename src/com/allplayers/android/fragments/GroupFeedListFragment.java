package com.allplayers.android.fragments;

import com.allplayers.android.widget.SimpleFeedAdapter;

import android.os.Bundle;
import android.support.v4.app.ListFragment;


public class GroupFeedListFragment extends ListFragment{

    /**
     * 
     */
    protected SimpleFeedAdapter mAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SimpleFeedAdapter(getActivity(), getLayout(), getFrom(), getTo());
    }

    private int[] getTo() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getFrom() {
        // TODO Auto-generated method stub
        return null;
    }

    private int getLayout() {
        // TODO Auto-generated method stub
        return 0;
    }






}
