package com.allplayers.android.fragments;

import com.allplayers.android.Globals;
import com.allplayers.android.GroupActivity;
import com.allplayers.android.GroupsMap;
import com.allplayers.android.LocalStorage;
import com.allplayers.android.R;
import com.allplayers.android.net.AuthClient;
import com.allplayers.objects.GroupData;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;


public class GroupListFragment extends ListFragment{
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
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, listContent));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (hasGroups) {
            GroupData group = groupList.get(position);

            Intent intent = new Intent(getActivity(), GroupActivity.class);
            intent.putExtra("GROUP", group);
            
            //Display the group page for the selected group
            getActivity().startActivity(intent);
        }
    }

    // TODO - this belongs in an abstract activity.
    //@Override
    //public boolean onKeyUp(int keyCode, KeyEvent event) {
    //    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
    //        startActivity(new Intent(GroupsActivity.this, FindGroupsActivity.class));
    //    }
    //
    //      return super.onKeyUp(keyCode, event);
    //}

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
                    jsonResult = LocalStorage.readUserGroups(getActivity());
                } else {
                    JSONArray result;
                    try {
                        AuthClient client = new AuthClient(GroupListFragment.this.getActivity());
                        // TODO - Store UUID on login.
                        String uuid = "2531d044-f611-11e0-a44b-12313d04fc0f";
                        result = client.index("users/" + uuid + "/groups", null);
                        jsonResult = result.toString();
                    } catch (OperationCanceledException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (AuthenticatorException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    LocalStorage.writeUserGroups(getActivity(), jsonResult, false);
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
                values = new String[] {getActivity().getString(R.string.empty_group_list)};
                hasGroups = false;
            }
            setListContent(values);
        }
    }
}
