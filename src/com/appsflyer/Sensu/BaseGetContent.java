package com.appsflyer.Sensu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by golan on 1/22/15.
 */
public class BaseGetContent {

    NetworkListener objectsReceived;
    Context mContext;

    public BaseGetContent(NetworkListener or) {
        objectsReceived = or;
    }


    public void fetchContent(String path, Context appContext) {
        mContext = appContext;

        SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String serverUrl = preferences.getString("server_url", "");
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }

        String url = serverUrl + path;
        new HttpAsyncTask().execute(url);
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private String getObjects(String url) {
        InputStream inputStream;
        String result;
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);

            SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);


            String username = preferences.getString("username", "");
            String password = preferences.getString("password", "");

            httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                if (httpResponse.getStatusLine().getStatusCode() == 401) {
                    // need login
                    result = "login needed!";
                }
                else {
                    result = convertInputStreamToString(inputStream);
                }
            } else
                result = "Error!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "login needed!";
        }

        return result;

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result =  getObjects(urls[0]);
            return result;

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            objectsReceived.gotNetworkObjects(result);
        }
    }

}