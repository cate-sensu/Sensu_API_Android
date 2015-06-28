package com.appsflyer.Sensu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import yaochangwei.pulltorefreshlistview.widget.RefreshableListView;

import java.util.ArrayList;

/**
 * Created by golan on 1/22/15.
 */
public class ClientsActivity extends Activity implements NetworkListener{

    JSONArray jsonArray;
    private BaseGetContent getContent;
    ClientAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients);

        final RefreshableListView list = (RefreshableListView) findViewById(R.id.clientsListView);
        getClients();

        list.setOnUpdateTask(new RefreshableListView.OnUpdateTask() {

            public void updateBackground() {
                getClients();
            }

            public void updateUI() {
                // aa.notifyDataSetChanged();
            }

            public void onUpdateStart() {

            }
        });

    }

    public void getClients() {

        getContent = new BaseGetContent(this);
        getContent.fetchContent("clients", getApplicationContext());
    }

    public void gotNetworkObjects(String responseData) {
        System.out.println("callback with data" + responseData);
        try {
            jsonArray = new JSONArray(responseData);
            Log.d("json array:", jsonArray.getString(0));

            ClientsActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    finishedTask();
                }
            });

        } catch (JSONException e) {
            Log.d("json parse ", e.getLocalizedMessage());
        }
    }

    public void finishedTask () {

        ArrayList<Client> newClients = Client.fromJson(jsonArray);
        mAdapter = new ClientAdapter(this, newClients);

        ListView listView = (ListView) findViewById(R.id.clientsListView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client selClient = mAdapter.getItem((int) id);
                Log.d ("sel client = ", selClient.clientName);

                Intent i = new Intent(getApplicationContext(), ClientDetailsActivity.class);
                i.putExtra("selected_client", selClient.rawJson.toString());
                startActivity(i);
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.clientsListView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Client obj = (Client) lv.getItemAtPosition(acmi.position);

            menu.add("Delete Client");

        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        Client obj = mAdapter.getItem(index);

        mAdapter.remove(obj);
        return true;


    }

}
