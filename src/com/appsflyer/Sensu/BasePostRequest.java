package com.appsflyer.Sensu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

/**
 * Created by golan on 1/27/15.
 */
public class BasePostRequest  {

    NetworkListener objectsReceived;
    Context mContext;
    public BasePostRequest(NetworkListener or) {
        objectsReceived = or;
    }

    public void postObject(String path, String body, Context context) {

        mContext = context;

        SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String serverUrl = preferences.getString("server_url", "");
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }

        String url = serverUrl + path;

        new HttpAsyncTask().execute(url, body);

    }

    private String makePostRequest(String path, String body) {

        String result = null;

        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(path);
        SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

        //Encoding POST data
        try {
            httpPost.setEntity(new StringEntity(body));
            httpPost.setHeader("Content-type", "application/json");

        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            InputStream inputStream;
            HttpResponse response = httpClient.execute(httpPost);

            Log.d("Http Post Response:", response.toString());
            inputStream = response.getEntity().getContent();
            if (inputStream != null) {
                if (response.getStatusLine().getStatusCode() == 401) {
                    // need login
                    result = "login needed!";
                }
                else {
                    result = convertInputStreamToString(inputStream);
                }
            } else
                result = "Error!";


        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

        return result;
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


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result =  makePostRequest(urls[0], urls[1]);
            return result;

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            objectsReceived.gotNetworkObjects(result);
        }
    }
}
