package com.allplayers.rest.resources;

import com.allplayers.rest.HttpClient;

public abstract class ClientResource {

    protected HttpClient mClient;

    public ClientResource(HttpClient client) {
        mClient = client;
    }
}
