package com.allplayers.android.fragments;

import com.allplayers.android.widget.SimpleFeedAdapter;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;

public abstract class LoaderListFragment extends ListFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST = 1;

    protected SimpleFeedAdapter mAdapter;
    /**
    protected Loadable mItems;
     **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
        mAdapter = new SimpleFeedAdapter(getActivity(), getLayout(), getFrom(), getTo());
        mItems = new Loadable(getSupportLoaderManager(), LOADER_LIST, new StatusViewManager(
                this, LOADER_LIST, this, this));

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ListDecorator(mAdapter, this));
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new ListScrollListener(mItems));

        mItems.init();
         **/
    }

    protected abstract String[] getProjection();

    protected abstract int getLayout();

    protected abstract String[] getFrom();

    protected abstract int[] getTo();

    //    /** {@inheritDoc} */
    //    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
    //        Intent intent = getActivity().getIntent();
    //        Uri uri = intent.getData();
    //        /**
    //        String selectionExtra = intent.getStringExtra(JamendoContract.EXTRA_SELECTION);
    //        QueryBuilder builder = new QueryBuilder(selectionExtra);
    //
    //        String[] projection = getProjection();
    //        String selection = builder.build();
    //        String[] selectionArgs = intent.getStringArrayExtra(JamendoContract.EXTRA_SELECTION_ARGS);
    //        String sortOrder = intent.getStringExtra(JamendoContract.EXTRA_SORT_ORDER);
    //        return new CursorLoader(this, uri, projection, selection, selectionArgs, sortOrder);
    //         **/
    //    }

    /** {@inheritDoc} */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    /** {@inheritDoc} */
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    /** {@inheritDoc} */
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Uri contentUri = this.getActivity().getIntent().getData();
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

}
