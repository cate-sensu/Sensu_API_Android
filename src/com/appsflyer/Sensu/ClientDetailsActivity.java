package com.appsflyer.Sensu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by golan on 2/1/15.
 */
public class ClientDetailsActivity extends Activity implements NetworkListener {

    BaseDeleteRequest baseDelete;
    Client mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details);


        Intent intent = getIntent();
        String client = intent.getStringExtra("selected_client");
        final TextView txClientName = (TextView) findViewById(R.id.txClientName);
        final TextView txAddress = (TextView) findViewById(R.id.txAddress);
        final TextView txSubs = (TextView) findViewById(R.id.txSubs);
        final TextView txVersion = (TextView) findViewById(R.id.txVersion);

        try {

            JSONObject clientJson = new JSONObject(client);
            mClient = new Client(clientJson);

            txClientName.setText(clientJson.getString("name"));
            txAddress.setText(clientJson.getString("address"));

            JSONArray subs = clientJson.getJSONArray("subscriptions");
            String sub = "";
            for (int i =0; i < subs.length(); ++i ) {
                String element = subs.getString(i);
                sub += element + ", ";
            }
            txSubs.setText(sub);

            txVersion.setText(clientJson.getString("version"));


        }catch (JSONException e) {
            Log.d("JSONException: ", e.getLocalizedMessage());
        }
    }

    public void onClientDetailsStash(View view) {
        Log.d("onDeleteClick", "");
        deleteClient();
    }


    private void deleteClient() {
        baseDelete = new BaseDeleteRequest(this);
        baseDelete.deleteObject(mClient.clientName, getApplicationContext());

    }

    public void finishedTask () {

        Toast.makeText(getApplicationContext(), "Delete Client succeeded", Toast.LENGTH_SHORT).show();
        this.finish();
    }


    public void gotNetworkObjects(String responseData) {
        System.out.println("callback with data" + responseData);

        ClientDetailsActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                finishedTask();
            }
        });
    }

}
