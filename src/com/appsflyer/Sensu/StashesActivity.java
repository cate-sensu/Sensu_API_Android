package com.appsflyer.Sensu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import yaochangwei.pulltorefreshlistview.widget.RefreshableListView;

import java.util.ArrayList;

/**
 * Created by golan on 1/22/15.
 */
public class StashesActivity extends Activity implements NetworkListener{

    private BaseGetContent getContent;
    JSONArray jsonArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stashes);

        final RefreshableListView list = (RefreshableListView) findViewById(R.id.stashesListView);

        getStashes();

        list.setOnUpdateTask(new RefreshableListView.OnUpdateTask() {

            public void updateBackground() {
                getStashes();
            }

            public void updateUI() {
                // aa.notifyDataSetChanged();
            }

            public void onUpdateStart() {

            }
        });

    }

    public void getStashes() {
        getContent = new BaseGetContent(this);
        getContent.fetchContent("stashes", getApplicationContext());
    }

    public void gotNetworkObjects(String responseData) {
        System.out.println("callback with data" + responseData);
        try {
            jsonArray = new JSONArray(responseData);
            Log.d("json array:", jsonArray.getString(0));

            StashesActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    finishedTask();
                }
            });

        } catch (JSONException e) {
            Log.d("json parse ", e.getLocalizedMessage());
        }
    }

    public void finishedTask () {

        ArrayList<Stash> newStashes = Stash.fromJson(jsonArray);
        StashAdapter adapter = new StashAdapter(this, newStashes);

        ListView listView = (ListView) findViewById(R.id.stashesListView);
        listView.setAdapter(adapter);
    }

}
