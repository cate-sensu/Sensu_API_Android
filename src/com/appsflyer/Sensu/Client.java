package com.appsflyer.Sensu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by golan on 1/25/15.
 */

public class Client {
    public String clientName;
    public String address;
    public String clientVersion;
    public JSONArray subscriptions;
    public JSONObject rawJson;

    public Client(JSONObject object) {
        try {
            this.clientName = object.getString("name");
            this.address = object.getString("address");
            this.clientVersion = object.getString("version");
            this.subscriptions = object.getJSONArray("subscriptions");
            this.rawJson = object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Client> fromJson(JSONArray jsonObjects) {
        ArrayList<Client> clients = new ArrayList<Client>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                clients.add(new Client(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return clients;
    }


}
