package com.allplayers.android;

import com.allplayers.objects.GroupData;
import com.allplayers.rest.RestApiV1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GroupsActivity extends ListActivity {
    private ArrayList<GroupData> groupList;
    private boolean hasGroups = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the list in a background task.
        new GroupsTask().execute();

    }

    /**
     * Helper to set the list content from an array.
     */
    protected void setListContent(String[] listContent) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listContent);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (hasGroups) {
            Globals.currentGroup = groupList.get(position);

            //Display the group page for the selected group
            Intent intent = new Intent(GroupsActivity.this, GroupPageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            startActivity(new Intent(GroupsActivity.this, FindGroupsActivity.class));
        }

        return super.onKeyUp(keyCode, event);
    }
    /**
     * Background task to load groups...
     */
    private class GroupsTask extends AsyncTask<String, String, ArrayList<GroupData>> {
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
        protected ArrayList<GroupData> doInBackground(String... args) {
            if (Globals.groupList.isEmpty()) {
                String jsonResult = "";

                //check local storage
                if (LocalStorage.getTimeSinceLastModification("UserGroups") / 1000 / 60 < 60) { //more recent than 60 minutes
                    jsonResult = LocalStorage.readUserGroups(getBaseContext());
                } else {
                    jsonResult = RestApiV1.getUserGroups();
                    LocalStorage.writeUserGroups(getBaseContext(), jsonResult, false);
                }

                GroupsMap groups = new GroupsMap(jsonResult);
                groupList = groups.getGroupData();
                Globals.groupList = groupList;
            } else {
                groupList = Globals.groupList;
            }

            return groupList;
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
        protected void onPostExecute(ArrayList<GroupData> groupList) {
            // TODO: Stop busy animation.
            String[] values;

            if (!groupList.isEmpty()) {
                values = new String[groupList.size()];

                for (int i = 0; i < groupList.size(); i++) {
                    values[i] = groupList.get(i).getTitle();
                }

                hasGroups = true;
            } else {
                values = new String[] {"no groups to display"};
                hasGroups = false;
            }
            setListContent(values);
        }
    }
}
