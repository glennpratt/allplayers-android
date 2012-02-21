package com.allplayers.android;

import com.allplayers.android.app.ActionBarActivity;
import com.allplayers.rest.RestApiV1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jasypt.util.text.BasicTextEncryptor;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends ActionBarActivity {
    private Context context;
    ProgressDialog progressDialog;

    /** called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressDialog = new ProgressDialog(this);

        context = this.getBaseContext();

        String storedUser = LocalStorage.readUserName(context);
        String storedPassword = LocalStorage.readPassword(context);
        String storedSecretKey = LocalStorage.readSecretKey(context);

        if (storedSecretKey == null || storedSecretKey.equals("")) {
            LocalStorage.writeSecretKey(context);
            storedSecretKey = LocalStorage.readSecretKey(context);
        }

        Globals.secretKey = storedSecretKey;

        if (storedUser != null && !storedUser.equals("") && storedPassword != null && !storedPassword.equals("")) {
            new LoginTask().execute(LocalStorage.readUserName(context), LocalStorage.readPassword(context));
        }

        final Button button = (Button)findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText usernameEditText = (EditText)findViewById(R.id.usernameField);
                EditText passwordEditText = (EditText)findViewById(R.id.passwordField);

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();;

                BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                textEncryptor.setPassword(LocalStorage.readSecretKey(context));
                String encryptedPassword = textEncryptor.encrypt(password);

                LocalStorage.writeUserName(context, username);
                LocalStorage.writePassword(context, password);

                new LoginTask().execute(username, encryptedPassword);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
            startActivity(new Intent(Login.this, FindGroupsActivity.class));
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * Background task to load groups...
     */
    private class LoginTask extends AsyncTask<String, String, String> {
        /**
         * Before jumping into background thread, start busy animation.
         */
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Signing in...");
            progressDialog.show();
        }

        /**
         * Perform the background login.
         */
        @Override
        protected String doInBackground(String... args) {
            String ret = RestApiV1.validateLogin(args[0], args[1]);
            return ret;
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
        protected void onPostExecute(String result) {
            // Stop animation.
            progressDialog.dismiss();

            try {
                JSONObject jsonResult = new JSONObject(result);
                RestApiV1.user_id = jsonResult.getJSONObject("user").getString("uuid");
            } catch (JSONException ex) {
                System.err.println("Login/user_id/" + ex);

                Toast invalidLogin = Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG);
                invalidLogin.show();
            }

            // Go to the home screen.
            startActivity(new Intent(Login.this, HomeActivity.class));
            finish();
        }
    }
}