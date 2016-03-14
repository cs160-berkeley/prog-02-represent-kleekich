package com.example.kangsik.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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


/**
 * Created by Kangsik on 3/3/16.
 */
public class PresidentialVoteActivity extends Activity {
    private String stringUrl;
    private String zipcode;
    private String longitude;
    private String latitude;
    private Boolean useZipCode;

    private String GOOGLE_API_KEY = "AIzaSyAhv4OMXSdVR-55Xh6VIzuy8a_NlI1nfmI";
    private String percentObama;
    private String percentRomney;
    private String state;
    private String countyLong;
    private String countyShort;
    private String countyData;
    private String stateData;

    private TextView personATextView;
    private TextView personBTextView;
    private TextView percentATextView;
    private TextView percentBTextView;
    private TextView stateTextView;
    private TextView districtTextView;


    private DownloadWebpageTask authTask;

    //To get county
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


            try{
            JSONObject jasonObject = new JSONObject(response);
            JSONArray jsonArrayAPI = jasonObject.optJSONArray("results");
            int arraySize = jsonArrayAPI.length();
                try {
                    if(response.contains("administrative_area_level_2")) {
                        for (int i = 0; i < arraySize; i++) {
                            JSONArray addressComponentsArray = jsonArrayAPI.getJSONObject(i).getJSONArray("address_components");
                            for (int j = 0; j < addressComponentsArray.length(); j++) {
                                JSONObject component = addressComponentsArray.getJSONObject(j);
                                JSONArray types = component.getJSONArray("types");
                                for (int k = 0; k < types.length(); k++) {
                                    String type = types.getString(k);
                                    if (type.equals("administrative_area_level_2")) {
                                        countyShort = component.getString("short_name");
                                    }else if (type.equals("administrative_area_level_1")) {
                                        state = component.getString("short_name");
                                    }
                                }
                            }
                        }

                        try {
                            InputStream stream = getAssets().open("election-county-2012.json");
                            int size = stream.available();
                            byte[] buffer = new byte[size];
                            stream.read(buffer);
                            stream.close();
                            String jsonString = new String(buffer, "UTF-8");
                            JSONArray jsonArrayDATA = new JSONArray(jsonString);
                            for (int i = 0; i < jsonArrayDATA.length(); i++) {
                                JSONObject j = jsonArrayDATA.getJSONObject(i);
                                countyData = j.getString("county-name");
                                //if(countyData.equals(countyShort) | countyData.equals(countyLong) ){
                                if (countyData.equals(countyShort)) {
                                    System.out.println("found it! obama-percentage" + j.get("obama-percentage") + " romney-percentage " + j.get("romney-percentage"));
                                    percentObama = j.getString("obama-percentage");
                                    percentRomney = j.getString("romney-percentage");

                                }
                            }
                        } catch (IOException e) {
                            System.out.println("====================");
                            System.out.println("READING VOTE FILE ERROR");
                            System.out.println("====================");
                        }
                        //IF COUNTY NOT FOUND
                    }else{
                        for (int i = 0; i < arraySize; i++) {
                            JSONArray addressComponentsArray = jsonArrayAPI.getJSONObject(i).getJSONArray("address_components");
                            for (int j = 0; j < addressComponentsArray.length(); j++) {
                                JSONObject component = addressComponentsArray.getJSONObject(j);
                                JSONArray types = component.getJSONArray("types");
                                for (int k = 0; k < types.length(); k++) {
                                    String type = types.getString(k);
                                    if (type.equals("locality")) {
                                        countyShort = component.getString("short_name");
                                    }
                                    else if (type.equals("administrative_area_level_1")) {
                                        state = component.getString("short_name");
                                    }
                                }
                            }
                        }
                        try {
                            InputStream stream = getAssets().open("election-county-2012.json");
                            int size = stream.available();
                            byte[] buffer = new byte[size];
                            stream.read(buffer);
                            stream.close();
                            String jsonString = new String(buffer, "UTF-8");
                            JSONArray jsonArrayDATA = new JSONArray(jsonString);
                            for (int i = 0; i < jsonArrayDATA.length(); i++) {
                                JSONObject j = jsonArrayDATA.getJSONObject(i);
                                stateData = j.getString("state-postal");
                                if(stateData.equals(state)){
                                    System.out.println("found it! obama-percentage" + j.get("obama-percentage") + " romney-percentage " + j.get("romney-percentage"));
                                    percentObama = j.getString("obama-percentage");
                                    percentRomney = j.getString("romney-percentage");
                                }
                            }

                        }catch(IOException ie){

                        }
                    }

                    personATextView.setText("Obama");
                    personBTextView.setText("Romney");
                    percentATextView.setText(percentObama);
                    percentBTextView.setText(percentRomney);
                    stateTextView.setText(state);
                    districtTextView.setText(countyShort);


                } catch (JSONException e) {
                    // Appropriate error handling code
                    System.out.println("================");
                    System.out.println("JSON OBJECT ERROR!!");
                    System.out.println("================");
                }
            }catch(JSONException je){
                System.out.println(je.getMessage());
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

        //if user used current location use latitude and longitude to get JSON array

        if(!zipcode.equals("-1")){
            useZipCode = true;
            stringUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+zipcode+"&region=us";
        }else{
            stringUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true";
            useZipCode = false;
        }


        // When user clicks button, calls AsyncTask.
        // Before attempting to fetch the URL, makes sure that there is a network connection.

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            authTask = new DownloadWebpageTask();
            authTask.execute(stringUrl);
            if (authTask != null && authTask.getStatus() == AsyncTask.Status.FINISHED) {
                //START YOUR NEW TASK HERE

            }
            else
            {
                //IGNORE BUTTON CLICK
            }




        } else {
            //textViewFoundBy.setText("No network connection available.");
        }

        //Get views;

        personATextView = (TextView) findViewById(R.id.personATextView);
        personBTextView = (TextView) findViewById(R.id.personBTextView);
        percentATextView = (TextView) findViewById(R.id.percentATextView);
        percentBTextView = (TextView) findViewById(R.id.percentBTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        districtTextView = (TextView) findViewById(R.id.districtTextView);


    };

}
