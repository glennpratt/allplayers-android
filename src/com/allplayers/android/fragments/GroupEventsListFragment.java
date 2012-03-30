package com.allplayers.android.fragments;

import com.allplayers.android.EventDisplayActivity;
import com.allplayers.android.EventsMap;
import com.allplayers.android.Globals;
import com.allplayers.objects.EventData;
import com.allplayers.rest.RestApiV1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class GroupEventsListFragment extends ListFragment{
    private ArrayList<EventData> eventsList;
    private boolean hasEvents = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the list in a background task.
        new GroupEventsTask().execute();

    }

    /**
     * Helper to set the list content from an array.
     */
    protected void setListContent(ArrayList<HashMap<String, String>> listContent) {
        String[] from = {"line1", "line2"};

        int[] to = {android.R.id.text1, android.R.id.text2};

        setListAdapter(new SimpleAdapter(getActivity(), listContent, android.R.layout.simple_list_item_2, from, to));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (hasEvents) {
            Globals.currentEvent = eventsList.get(position);

            //Display the group page for the selected group
            getActivity().startActivity(new Intent(getActivity(), EventDisplayActivity.class));
        }
    }

    /**
     * Background task to load groups...
     */
    private class GroupEventsTask extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {
        /**
         * Before jumping into background thread, start busy animation.
         */
        @Override
        protected void onPreExecute() {
            // TODO: Add busy animation.
        }

        /**
         * Perform the background query using {@link ExtendedWikiHelper}, which
         * may return an error message as the result.
         */
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... args) {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(2);
            String jsonResult = RestApiV1.getGroupEventsByGroupId(Globals.currentGroup.getUUID());

            EventsMap events = new EventsMap(jsonResult);
            eventsList = events.getEventData();
            HashMap<String, String> map;

            if (!eventsList.isEmpty()) {
                for (int i = 0; i < eventsList.size(); i++) {
                    map = new HashMap<String, String>();
                    map.put("line1", eventsList.get(i).getTitle());

                    String start = eventsList.get(i).getStartDateString();
                    map.put("line2", start);
                    list.add(map);
                }

                hasEvents = true;
            } else {
                map = new HashMap<String, String>();
                map.put("line1", "No events to display.");
                map.put("line2", "");
                list.add(map);
                hasEvents = false;
            }

            return list;
        }

        /**
         * Progress update (needs research).
         */
        @Override
        protected void onProgressUpdate(String... args) {
            // TODO: Update busy animation.
        }

        /**
         * Finished, put the content in.
         */
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> eventsList) {
            setListContent(eventsList);
        }
    }
}
