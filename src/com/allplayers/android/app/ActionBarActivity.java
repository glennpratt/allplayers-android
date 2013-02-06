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
import com.allplayers.android.account.Authenticator;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

/**
 * A base activity that defers common functionality across app activities to an {@link
 * ActionBarHelper}.
 *
 * NOTE: dynamically marking menu items as invisible/visible is not currently supported.
 *
 */
public abstract class ActionBarActivity extends SherlockFragmentActivity {
    private void verifyAccount() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(Authenticator.ACCOUNT_TYPE);

        if (accounts.length > 0) {
            // TODO - Support multiple accounts by preference.
            Account account = accounts[0];
        }
        else {
            // Start the activity to add an account.
            // TODO - Record account deletion or change where this is called,
            // because if an activity is still in memory, this wont be called.
            am.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, this, null, null);
        }
    }

    //    /**{@inheritDoc}*/
    //    @Override
    //    public MenuInflater getMenuInflater() {
    //        return mActionBarHelper.getMenuInflater(super.getMenuInflater());
    //    }

    /**{@inheritDoc}*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Check account state first.
        verifyAccount();
        super.onCreate(savedInstanceState);
        //mActionBarHelper.onCreate(savedInstanceState);
    }

    /**{@inheritDoc}*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save")
        .setIcon(R.drawable.ic_compose)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add("Search")
        .setIcon(R.drawable.ic_search)
        .setActionView(R.layout.collapsible_edittext)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add("Settings")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    //public boolean onOptionsItemSelected(MenuItem item) {
    //        switch (item.getItemId()) {
    //        case android.R.id.home:
    //            Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
    //            break;
    //
    //        case R.id.menu_refresh:
    //            Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
    //            getActionBarHelper().setRefreshActionItemState(true);
    //            getWindow().getDecorView().postDelayed(
    //                    new Runnable() {
    //                        public void run() {
    //                            getActionBarHelper().setRefreshActionItemState(false);
    //                        }
    //                    }, 1000);
    //            break;
    //
    //        case R.id.menu_search:
    //            //Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
    //            // temp
    //            startActivity(new Intent(this, FragmentTabsPager.class));
    //            break;
    //
    //        case R.id.menu_account:
    //            Toast.makeText(this, "Tapped account", Toast.LENGTH_SHORT).show();
    //            startActivity(new Intent(this, AuthenticatorActivity.class));
    //            break;
    //        }
    //        return super.onOptionsItemSelected(item);
    //    }

    //    /**
    //     * A fragment that displays a menu.  This fragment happens to not
    //     * have a UI (it does not implement onCreateView), but it could also
    //     * have one if it wanted.
    //     */
    //    public static class MenuFragment extends SherlockFragment {
    //
    //        @Override
    //        public void onCreate(Bundle savedInstanceState) {
    //            super.onCreate(savedInstanceState);
    //            setHasOptionsMenu(true);
    //        }
    //
    //        @Override
    //        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //            menu.add("Menu 1a").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    //            menu.add("Menu 1b").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    //        }
    //    }
}
