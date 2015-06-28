package com.appsflyer.Sensu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by golan on 1/25/15.
 */
public class Check {


    public String checkName;
    public JSONArray handlers;
    public String command;
    public int interval;
    public int occurrences;
    public JSONArray subscribers;

    public Check(JSONObject object) {
        try {
            this.checkName = object.getString("name");
            this.handlers = object.getJSONArray("handlers");
            this.command = object.getString("command");
            this.interval = object.getInt("interval");
            this.occurrences = object.getInt("occurrences");
            this.subscribers = object.getJSONArray("subscribers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Check> fromJson(JSONArray jsonObjects) {
        ArrayList<Check> checks = new ArrayList<Check>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                checks.add(new Check(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return checks;
    }

}
