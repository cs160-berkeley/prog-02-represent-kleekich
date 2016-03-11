package com.example.kangsik.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kangsik on 3/3/16.
 */
public class PresidentialVoteActivity extends Activity {
    private String stringUrlZIP;
    private String stringUrlLL;
    private String zipcode;
    private String longitude;
    private String latitude;
    private String GOOGLE_API_KEY = "AIzaSyAhv4OMXSdVR-55Xh6VIzuy8a_NlI1nfmI";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential_vote);
        //dummy data


        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        String selectedLocation = extras.getString("selectedLocation");
        zipcode = extras.getString("ZIPCODE");
        longitude = extras.getString("LONGITUDE");
        latitude = extras.getString("LATITUDE");


        stringUrlZIP = "http://maps.googleapis.com/maps/api/geocode/json?address="+zipcode+"&region=us&key="+GOOGLE_API_KEY;
        stringUrlLL = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true"+GOOGLE_API_KEY;


        class DownloadWebpageTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {

                // params comes from the execute() call: params[0] is the url.
                try {
                    return downloadUrl(urls[0]);
                } catch (IOException e) {
                    return "Unable to retrieve web page. URL may be invalid.";
                }
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String response) {
                //textView.setText(result);
                //parse json response
                if (response == null) {
                    response = "THERE WAS AN ERROR";
                }
                Log.i("INFO", response);
                try {
                    //For multiple representatives
                    Gson gson = new Gson();
                    String jsonStringArray = gson.toJson(reps);
                    JSONObject jasonObject = new JSONObject(response);
                    JSONArray jsonArray = jasonObject.optJSONArray("results");
                    Gson gson = new Gson();
                    String jsonStringArray = gson.toJson(reps);

                } catch (JSONException e) {
                    // Appropriate error handling code
                    System.out.println("================");
                    System.out.println("JSON OBJECT ERROR!!");
                    System.out.println("================");
                }
            }

            // Given a URL, establishes an HttpUrlConnection and retrieves
            // the web page content as a InputStream, which it returns as
            // a string.
            private String downloadUrl(String myurl) throws IOException {
                try {
                    URL url = new URL(myurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            }
        }

        // When user clicks button, calls AsyncTask.
        // Before attempting to fetch the URL, makes sure that there is a network connection.

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            textViewFoundBy.setText("No network connection available.");
        }





        try{
            InputStream stream = getAssets().open("election-county-2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");


            try{
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject j = (JSONObject) jsonArray.get(i);
                    if(j.get("county-name").equals(county)){
                        System.out.println("found it! obama-percentage" + j.get("obama-percentage") + " romney-percentage " + j.get("romney-percentage"));
                    }
                }
            }catch(JSONException e){
                System.out.println("====================");
                System.out.println("JSON ARRAY ERROR");
                System.out.println("====================");
            }

        }catch(IOException e){
            System.out.println("====================");
            System.out.println("READING VOTE FILE ERROR");
            System.out.println("====================");
        }




        //Get views;

        TextView personATextView = (TextView) findViewById(R.id.personATextView);
        TextView personBTextView = (TextView) findViewById(R.id.personBTextView);
        TextView percentATextView = (TextView) findViewById(R.id.percentATextView);
        TextView percentBTextView = (TextView) findViewById(R.id.percentBTextView);
        TextView stateTextView = (TextView) findViewById(R.id.stateTextView);
        TextView districtTextView = (TextView) findViewById(R.id.districtTextView);

        if(selectedLocation.equals("11111")){

            personATextView.setText("Obama");
            personBTextView.setText("Romney");
            percentATextView.setText("36%");
            percentBTextView.setText("28%");
            stateTextView.setText("State: California");
            districtTextView.setText("District: 11th");

        }else if(selectedLocation.equals("94704")) {
            personATextView.setText("Joey");
            personBTextView.setText("Michael");
            percentATextView.setText("52%");
            percentBTextView.setText("24%");
            stateTextView.setText("State: Alaska");
            districtTextView.setText("District: Eskimo");

            //districtTextView.setText("District: " );
        }
        /*

        System.out.println("============================================");
        System.out.println(selectedLocation);
        System.out.println("============================================");
        personATextView.setText("1");
        personBTextView.setText("12314");
        percentATextView.setText("125");
        percentBTextView.setText("15");
        stateTextView.setText("State");
        districtTextView.setText("1515");

*/


    };

}
