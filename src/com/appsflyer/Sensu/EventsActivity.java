package com.appsflyer.Sensu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import yaochangwei.pulltorefreshlistview.widget.RefreshableListView;
import yaochangwei.pulltorefreshlistview.widget.RefreshableListView.OnUpdateTask;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by golan on 1/22/15.
 */
public class EventsActivity extends Activity implements NetworkListener{

    static JSONArray jsonArray;
    private BaseGetContent getContent;
    private RefreshableListView mListView;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        final RefreshableListView list = (RefreshableListView) findViewById(R.id.eventListView);
        mListView = list;

        mListView.setEmptyView(findViewById(R.id.emptyElement));

        getEvents();

        list.setOnUpdateTask(new OnUpdateTask() {

            public void updateBackground() {
                getEvents();
            }

            public void updateUI() {
                // aa.notifyDataSetChanged();
            }

            public void onUpdateStart() {

            }
        });

    }

    public void getEvents() {
        getContent = new BaseGetContent(this);
        getContent.fetchContent("events", getApplicationContext());
    }

    public void finishedTask () {


        ArrayList<Event> newEvents = Event.fromJson(jsonArray);

        Collections.sort(newEvents);

        final EventAdapter adapter = new EventAdapter(this, newEvents);


        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selEvent = adapter.getEventById((int) id);
                Log.d("self event = ", selEvent.eventName);

                Intent i = new Intent(getApplicationContext(), EventDetailsActivity.class);
                i.putExtra("selected_event", selEvent.rawJson.toString());
                startActivity(i);
            }
        });

    }

    public void gotNetworkObjects(String responseData) {
        System.out.println("callback with data" + responseData);

        if (responseData.equals("login needed!")) {
            showLoginDialog();
        }
        else {
            try {
                jsonArray = new JSONArray(responseData);
                Log.d("json array:", jsonArray.getString(0));

                EventsActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    finishedTask();
                }
            });

            } catch (JSONException e) {
                Log.d("json parse ", e.getLocalizedMessage());
            }
        }
    }


    private void showLoginDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);
        final EditText server = (EditText) prompt.findViewById(R.id.sensu_server);

        alertDialogBuilder.setTitle("LOGIN");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String password = pass.getText().toString();
                        String username = user.getText().toString();
                        String server_url = server.getText().toString();

                        try
                        {
                            if ( username.length()<2 || password.length()<2)
                            {
                                Toast.makeText(EventsActivity.this,"Invalid username or password", Toast.LENGTH_LONG).show();
                                showLoginDialog();
                            }
                            else {
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("server_url", server_url);
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.commit();
                                getEvents();
                            }
                        }catch(Exception e)
                        {
                            Toast.makeText(EventsActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        alertDialogBuilder.show(


    );
    }

}



