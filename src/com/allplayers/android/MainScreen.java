package com.allplayers.android;

import java.io.IOException;

import com.allplayers.android.account.Authenticator;
import com.allplayers.rest.RestApiV1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainScreen extends TabActivity {
    private Context context;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Check account state first.
        verifyAccount();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inapplayout);
    }

    public void onLogin() {

        context = this.getBaseContext();

        Resources res = getResources(); // Resource object to get Drawables, this will be little icons for each one
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, GroupsActivity.class); //set as GroupsActivity.class, this will be changed to
        //whatever we end up calling that particular class

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("groups").setIndicator("Groups",
                res.getDrawable(R.drawable.ic_tab_groups)).setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, MessageActivity.class);
        spec = tabHost.newTabSpec("messages").setIndicator("Messages",
                res.getDrawable(R.drawable.ic_tab_messages)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PhotosActivity.class);
        spec = tabHost.newTabSpec("photos").setIndicator("Photos",
                res.getDrawable(R.drawable.ic_tab_photos)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, EventsActivity.class);
        spec = tabHost.newTabSpec("events").setIndicator("Events",
                res.getDrawable(R.drawable.ic_tab_events)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.defaultmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.logOut: {
            RestApiV1.logOut();
            LocalStorage.writePassword(context, "");
            startActivity(new Intent(MainScreen.this, Login.class));
            finish();
            return true;
        }
        case R.id.search: {
            startActivity(new Intent(MainScreen.this, FindGroupsActivity.class));
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Verify an account is available to be used or prompt the user to choose
     * or create one.
     *
     * TODO - Implement this on all Activities, one way or another, so that
     *  Activities may be started by arbitrary intent and still get an account.
     *
     * TODO - If an Activity is still in memory, this isn't called
     *  (onCreate). So a user may delete an account and continue using
     *  the app logged in.  Notify or check more often.
     */
    private void verifyAccount() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(Authenticator.ACCOUNT_TYPE);

        if (accounts.length > 0) {
            new AuthTask().execute(accounts[0]);
        }
        else {
            // Start the activity to add an account.
            am.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, this, null, null);
        }
    }

    /**
     * Background task to load groups...
     */
    private class AuthTask extends AsyncTask<Account, Void, Void> {
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
        protected Void doInBackground(Account... accounts) {
            AccountManager am = AccountManager.get(MainScreen.this);
            Account account = accounts[0];
            try {
                String authToken = am.getAuthToken(account, Authenticator.ACCOUNT_TYPE, false, null, null).getResult().getString(AccountManager.KEY_AUTHTOKEN);
                RestApiV1.restoreCookies(authToken);
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
            return null;
        }

        /**
         * Progress update (needs research).
         */
        @Override
        protected void onProgressUpdate(Void... args) {
        }

        /**
         * Finished, put the content in.
         */
        @Override
        protected void onPostExecute(Void list) {
            MainScreen.this.onLogin();
        }
    }
}