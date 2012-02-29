package com.allplayers.android.account;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;
import org.json.JSONObject;

import com.allplayers.android.R;
import com.allplayers.rest.RestApiV1;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Authenticator extends AbstractAccountAuthenticator {

    /**
     * TODO - Duplicate of string res/xml/authenticator.xml.
     */
    public static final String ACCOUNT_TYPE = "com.allplayers.android";

    /**
     *  The tag used to log to adb console.
     */
    private static final String TAG = "Authenticator";

    // Authentication Activity context
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);
        // Expose the context locally, since it is private in super.
        mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
            String authTokenType, String[] requiredFeatures, Bundle options) {
        Log.v(TAG, "addAccount()");
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        AccountManager am = AccountManager.get(mContext);
        String username = account.name;
        String password = am.getPassword(account);

        if (password != null) {
            String authToken;
            try {
                RestApiV1 client = new RestApiV1();
                JSONObject loginResponse = client.userLogin(username, password);
                try {
                    // TODO - This was null last run...
                    authToken = loginResponse.getString("authToken");
                } catch (JSONException ex) {
                    // TODO Auto-generated catch block
                    throw new RuntimeException("JSONException", ex);
                }

                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                return result;
            }
            catch (RuntimeException ex) {
                ex.printStackTrace();
                // authToken failed, prompt for creds.
                // TODO - Distinguish between temporary network failures and permenant auth errors.
            }
        }

        // Password missing or login failed.
        return updateCredentials(response, account, authTokenType, options);
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (authTokenType == ACCOUNT_TYPE) {
            // Label the account the same as the application.
            return mContext.getString(R.string.app_name);
        }
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

}
