package com.allplayers.android.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

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
        mAuthToken = am.getAuthToken(account, Authenticator.ACCOUNT_TYPE, false, null, null).getResult().getString(AccountManager.KEY_AUTHTOKEN);
        System.out.println("hi mom");
    }

    @Override
    protected void injectAuthToken(HttpURLConnection connection) {
        JSONObject auth;
        String cookie;
        try {
            auth = new JSONObject(mAuthToken);
            cookie = getCookieLogin(auth);
        } catch (JSONException e) {
            throw new RuntimeException("Invalid Auth Token");
        }

        // TODO - Parse token!
        connection.addRequestProperty("Cookie", cookie);
    }

    protected String getCookieLogin(JSONObject response) throws JSONException {
        String test = response.getString("user");
        String sessionCookie = response.getString("session_name") + "=" + response.getString("sessid");
        String ssoCookie = "fake=fake";

        return sessionCookie + ";" + ssoCookie;
    }

    protected void invalidate() {
        mAm.invalidateAuthToken(Authenticator.ACCOUNT_TYPE, mAuthToken);
    }

}
