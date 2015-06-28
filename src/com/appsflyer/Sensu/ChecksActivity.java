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
public class ChecksActivity extends Activity implements NetworkListener {

    JSONArray jsonArray;
    private BaseGetContent getContent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checks);

        final RefreshableListView list = (RefreshableListView) findViewById(R.id.checksListView);

        getChecks();

        list.setOnUpdateTask(new RefreshableListView.OnUpdateTask() {

            public void updateBackground() {
                getChecks();
            }

            public void updateUI() {
                // aa.notifyDataSetChanged();
            }

            public void onUpdateStart() {

            }
        });

    }

    public void getChecks() {
        getContent = new BaseGetContent(this);
        getContent.fetchContent("checks", getApplicationContext());
    }

    public void gotNetworkObjects(String responseData) {
        System.out.println("callback with data" + responseData);
        try {
            jsonArray = new JSONArray(responseData);
            Log.d("json array:", jsonArray.getString(0));

            ChecksActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    finishedTask();
                }
            });

        } catch (JSONException e) {
            Log.d("json parse ", e.getLocalizedMessage());
        }
    }

    public void finishedTask () {

        ArrayList<Check> newChecks = Check.fromJson(jsonArray);
        CheckAdapter adapter = new CheckAdapter(this, newChecks);

        ListView listView = (ListView) findViewById(R.id.checksListView);
        listView.setAdapter(adapter);

    }

}
