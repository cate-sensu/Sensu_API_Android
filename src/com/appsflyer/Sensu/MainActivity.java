package com.appsflyer.Sensu;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Events");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Checks");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Stashes");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Clients");

        tab1.setIndicator("Events");
        tab1.setContent(new Intent(this,EventsActivity.class));

        tab2.setIndicator("Checks");
        tab2.setContent(new Intent(this,ChecksActivity.class));

        tab3.setIndicator("Stashes");
        tab3.setContent(new Intent(this,StashesActivity.class));

        tab4.setIndicator("Clients");
        tab4.setContent(new Intent(this,ClientsActivity.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);


        AppsFlyerLib.setAppsFlyerKey("rbz2mfgZQY5mSEYNTyjwni");
        AppsFlyerLib.setCurrencyCode("USD");
        HashMap<String, Object> CustomerData = new HashMap<String,Object>();
        CustomerData.put("consumer_id", "123");
        CustomerData.put("location", "12312313,12342422");
        AppsFlyerLib.setAdditionalData(CustomerData);

        AppsFlyerLib.sendTracking(getApplicationContext());


        AppsFlyerLib.registerConversionListener(this,new AppsFlyerConversionListener() {

            @Override
            public void onInstallConversionDataLoaded( Map <String, String> conversionData) {

                for (String attrName : conversionData.keySet()){
                    Log.d("AppsFlyerTest","attribute: "+attrName+" = "+conversionData.get(attrName));

                }
            }
            @Override
            public void onInstallConversionFailure(String errorMessage) {
                Log.d("AppsFlyerTest", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        });


        }





}
