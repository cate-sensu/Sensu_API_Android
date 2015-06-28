package com.appsflyer.Sensu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by golan on 1/26/15.
 */


public class EventDetailsActivity extends Activity implements NetworkListener{
    BasePostRequest basePost;
    Event mEvent;
    String stashReason;
    int expire;

    static final int fifteen_minutes = 15*60;
    static final int one_hour = 60*60;
    static final int one_day = 60*60*24*365;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        Intent intent = getIntent();
        String event = intent.getStringExtra("selected_event");
        final TextView txEventName = (TextView) findViewById(R.id.txEventName);
        final TextView txEventMachine = (TextView) findViewById(R.id.txEventMachine);
        final TextView txEventHistory = (TextView) findViewById(R.id.txEventHistory);
        final TextView txIssued = (TextView) findViewById(R.id.txIssued);
        final TextView txExec = (TextView) findViewById(R.id.txExec);
        final TextView txOccur = (TextView) findViewById(R.id.txOccur);
        final TextView txHandlers = (TextView) findViewById(R.id.txHandlers);
        try {
            
            JSONObject eventJson = new JSONObject(event);
            mEvent = new Event(eventJson);
            txEventName.setText(eventJson.getJSONObject("client").getString("name"));

            String machineName = eventJson.getJSONObject("check").getString("name");
            txEventMachine.setText(machineName);

            JSONArray history = eventJson.getJSONObject("check").getJSONArray("history");
            String txHist = "";
            for (int i =0; i < history.length(); ++i ) {
                String element = history.getString(i);
                txHist += element + ", ";
            }
            txEventHistory.setText(txHist);

            long exec = eventJson.getJSONObject("check").getLong("executed");
            Date date = new Date(exec);
            txExec.setText(date.toString());

            txOccur.setText(eventJson.getString("occurrences"));

            long issued = eventJson.getJSONObject("check").getLong("issued");
            Date issuedDate = new Date(issued);
            txIssued.setText(issuedDate.toString());

            JSONArray handlers = eventJson.getJSONObject("check").getJSONArray("handlers");
            String tempHandlers = "";
            for (int i =0; i < handlers.length(); ++i ) {
                String element = handlers.getString(i);
                tempHandlers += element + ", ";
            }
            txHandlers.setText(tempHandlers);

        }catch (JSONException e) {
            Log.d ("JSONException: ", e.getLocalizedMessage());
        }

    }

    public void onStashClick(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(this);
        tv.setText("Stash Event");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        final EditText reasonInput = new EditText(this);
        //stashReason = et.getText().toString();
        TextView tv1 = new TextView(this);
        tv1.setText("Reason:");

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        layout.addView(tv1,tv1Params);
        layout.addView(reasonInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        String[] options = { "15 min", "1 Hour", "24 Hours", "No Expiration"};


        alertDialogBuilder.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 15 min
                        expire = fifteen_minutes;
                        break;
                    case 1: // 1 hour
                        expire = one_hour;
                        break;
                    case 2: // 24 hours
                        expire = one_day;
                        break;
                    case 3: // no Expire
                        expire = -1;
                        break;
                    default:
                        break;
                }
            }
        });


        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setTitle("stash");
        alertDialogBuilder.setCustomTitle(tv);

        alertDialogBuilder.setCancelable(false);


        // Setting Negative "Cancel" Button
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {
            public void onClick (DialogInterface dialog,int whichButton){
                        dialog.cancel();
            }
        });

         // Setting Positive "Yes" Button
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    public void onClick (DialogInterface dialog,int which){
                        stashReason = reasonInput.getText().toString();
                        stashEvent();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();

            try {
                alertDialog.show();
            }

            catch(Exception e)
            {
                // WindowManager$BadTokenException will be caught and the app would
                // not display the 'Force Close' message
                e.printStackTrace();
            }
        }

    private void stashEvent() {
        try {
            String jsonString = String.format("{\"path\": \"silence/%s\",\"content\": {\"reason\": \"%s\" },\"expire\": %d }", mEvent.client.getString("name"),
                    stashReason, expire);

            basePost = new BasePostRequest(this);
            basePost.postObject("stashes", jsonString, getApplicationContext());

        }
        catch (JSONException e) {

        }
    }


    public void finishedTask () {

        Toast.makeText(getApplicationContext(), "Stash success", Toast.LENGTH_SHORT).show();
        this.finish();
    }


    public void gotNetworkObjects(String responseData) {
        System.out.println("callback with data" + responseData);

        EventDetailsActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                finishedTask();
            }
        });
    }
}
