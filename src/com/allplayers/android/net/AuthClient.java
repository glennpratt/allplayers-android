package com.allplayers.android.net;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import com.allplayers.android.account.Authenticator;
import com.allplayers.rest.RestApiV1;

/**
 * Make authenticated requests to AllPlayers.com using an account from
 * Android AccountManager.
 */
public class AuthClient extends RestApiV1 {

    private AccountManager mAm;
    private Account mAccount;
    private String mAuthToken;

    public AuthClient(Context context) throws OperationCanceledException, AuthenticatorException, IOException {
        mAm = AccountManager.get(context);
        mAccount = mAm.getAccountsByType(Authenticator.ACCOUNT_TYPE)[0];
        mAuthToken = mAm.getAuthToken(mAccount, Authenticator.ACCOUNT_TYPE, false, null, null).getResult().getString(AccountManager.KEY_AUTHTOKEN);
        restoreCookies(mAuthToken);
    }

    /**
     * Never use this on the main thread.
     *
     * @param am
     *   Passing the AccountManager in allows us to query for auth tokens and prompt the user to login
     *   as needed.
     * @param account
     *   The active AllPlayers.com AccountManager Account.
     * @throws IOException
     * @throws AuthenticatorException
     * @throws OperationCanceledException
     */
    public AuthClient(AccountManager am, Account account) throws OperationCanceledException, AuthenticatorException, IOException {
        mAm = am;
        mAccount = account;
        mAuthToken = mAm.getAuthToken(mAccount, Authenticator.ACCOUNT_TYPE, false, null, null).getResult().getString(AccountManager.KEY_AUTHTOKEN);
        restoreCookies(mAuthToken);
        // Invalidate authtoken every time for testing.
        //invalidate();
        //mAuthToken = am.getAuthToken(account, Authenticator.ACCOUNT_TYPE, false, null, null).getResult().getString(AccountManager.KEY_AUTHTOKEN);
        //System.out.println("mAuthToken: " + mAuthToken);
    }

    protected void restoreCookies(String jsonCookies) {
        // TODO - handle cookie changes!
        JSONArray cookies;
        try {
            cookies = new JSONArray(jsonCookies);

            for (int i = 1; i < cookies.length(); i++) {
                String cookie = cookies.getString(i);
                mCookies.add(cookie);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Invalid Auth Token");
        }
    }

    protected void invalidate() {
        mAm.invalidateAuthToken(Authenticator.ACCOUNT_TYPE, mAuthToken);
    }

}
