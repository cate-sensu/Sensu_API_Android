package com.appsflyer.Sensu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by golan on 1/21/15.
 */


public class Event implements Comparable<Event>
{
    public String eventName;
    public String action;
    public int status;
    public JSONObject check;
    public JSONObject client;
    public JSONObject rawJson;

    public Event(JSONObject object) {
        this.rawJson = object;
        try {
            this.check = object.getJSONObject("check");
            this.client = object.getJSONObject("client");
            this.action = object.getString("action");
            this.eventName = check.getString("name");
            this.status = check.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Event> fromJson(JSONArray jsonObjects) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                events.add(new Event(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }

    @Override
    public int compareTo(Event e) {
        int compareStatus = e.status;
        return this.status-compareStatus;
    }
}
