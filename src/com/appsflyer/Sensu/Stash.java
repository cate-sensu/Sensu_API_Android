package com.appsflyer.Sensu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by golan on 1/25/15.
 */
public class Stash {

    public String stashPath;


    public Stash(JSONObject object) {
        try {
            this.stashPath = object.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Stash> fromJson(JSONArray jsonObjects) {
        ArrayList<Stash> stashes = new ArrayList<Stash>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                stashes.add(new Stash(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stashes;
    }
}
