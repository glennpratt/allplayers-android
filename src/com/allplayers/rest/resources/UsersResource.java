package com.allplayers.rest.resources;

import org.json.JSONException;
import org.json.JSONObject;

import com.allplayers.rest.HttpClient;

public class UsersResource extends ClientResource {

  public UsersResource(HttpClient client) {
      super(client);
  }

  /**
   * Start an authenticated session with the given credentials.
   *
   * @param email
   * @param password
   * @return
   * @throws JSONException
   */
  public JSONObject login(String email, String password) throws JSONException {
      JSONObject data = new JSONObject().put("username", email).put("password", password);
      return mClient.post("users/login", null, data);
  }
}
