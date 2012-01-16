/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.allplayers.android.app;

import com.allplayers.android.R;
import com.allplayers.android.fragments.FragmentTabsPager;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * A base activity that defers common functionality across app activities to an {@link
 * ActionBarHelper}.
 *
 * NOTE: dynamically marking menu items as invisible/visible is not currently supported.
 *
 * NOTE: this may used with the Android Compatibility Package by extending
 * android.support.v4.app.FragmentActivity instead of {@link Activity}.
 */
public abstract class ActionBarActivity extends FragmentActivity {
    final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);

    /**
     * Returns the {@link ActionBarHelper} for this activity.
     */
    protected ActionBarHelper getActionBarHelper() {
        return mActionBarHelper;
    }

    /**{@inheritDoc}*/
    @Override
    public MenuInflater getMenuInflater() {
        return mActionBarHelper.getMenuInflater(super.getMenuInflater());
    }

    /**{@inheritDoc}*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBarHelper.onCreate(savedInstanceState);
    }

    /**{@inheritDoc}*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarHelper.onPostCreate(savedInstanceState);
    }

    /**
     * Base action bar-aware implementation for
     * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
     *
     * Note: marking menu items as invisible/visible is not currently supported.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = false;

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);

        retValue |= mActionBarHelper.onCreateOptionsMenu(menu);
        retValue |= super.onCreateOptionsMenu(menu);

        return retValue;
    }

    /**{@inheritDoc}*/
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        mActionBarHelper.onTitleChanged(title, color);
        super.onTitleChanged(title, color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
            break;

        case R.id.menu_refresh:
            Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
            getActionBarHelper().setRefreshActionItemState(true);
            getWindow().getDecorView().postDelayed(
                    new Runnable() {
                        public void run() {
                            getActionBarHelper().setRefreshActionItemState(false);
                        }
                    }, 1000);
            break;

        case R.id.menu_search:
            //Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
            // temp
            startActivity(new Intent(this, FragmentTabsPager.class));
            break;

        case R.id.menu_share:
            Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
