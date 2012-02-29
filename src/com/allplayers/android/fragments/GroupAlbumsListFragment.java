package com.allplayers.android.fragments;

import java.util.ArrayList;

import com.allplayers.android.AlbumAdapter;
import com.allplayers.android.AlbumsMap;
import com.allplayers.android.Globals;
import com.allplayers.android.R;
import com.allplayers.objects.AlbumData;
import com.allplayers.rest.RestApiV1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class GroupAlbumsListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the list in a background task.
        new GroupAlbumsTask().execute();

    }

    /**
     * Helper to set the list content from an array.
     */
    protected void setListContent(ArrayList<AlbumData> albumList) {
        if (albumList.isEmpty()) {
            String[] values = new String[] {"no albums to display"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        } else {
            //Create a customized ArrayAdapter
            AlbumAdapter adapter = new AlbumAdapter(getActivity(), R.layout.albumlistitem, albumList);
            setListAdapter(adapter);
        }
    }
    /**
     * Background task to load groups...
     */
    private class GroupAlbumsTask extends AsyncTask<String, String, ArrayList<AlbumData>> {
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
        protected ArrayList<AlbumData> doInBackground(String... args) {
            String jsonResult = RestApiV1.getGroupAlbumsByGroupId(Globals.currentGroup.getUUID());
            AlbumsMap albums = new AlbumsMap(jsonResult);
            ArrayList<AlbumData> list = albums.getAlbumData();

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
        protected void onPostExecute(ArrayList<AlbumData> list) {
            setListContent(list);
        }
    }
}
