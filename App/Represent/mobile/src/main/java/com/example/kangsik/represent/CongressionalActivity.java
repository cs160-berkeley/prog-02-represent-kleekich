package com.example.kangsik.represent;

/**
 * Created by Kangsik on 3/2/16.
 */

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CongressionalActivity extends Activity {
    private Context cCtx;
    private String selectedLocation;
    private ArrayAdapter repAdapter;
    private ListView repListView;
    private String zipcode;
    private String longitude;
    private String latitude;

    private String stringUrl;

    private final static String SUNLIGHT_API_KEY = "45a993c1ef534c45b4b15bfc6ead5422";
    private static final String TWITTER_KEY = "4Jv7s0n2tUqSdM4FsiQ2aZGuw";
    private static final String TWITTER_SECRET = "gw2MY2JZHdlpS18sLMsV1dhYKPOGI4B8bra7PGET8Fm7wKxlV8";


    private TextView textViewSearchMode;
    private TextView textViewFoundBy;
    private TextView textViewResponse;

    //FOR REPRESENTATIVE
    private String name;
    private String bid;
    private String first_name;
    private String last_name;
    private String party;
    private String title;
    private String email;
    private String website;
    private String endTerm;
    private String twitterId;
    private String committee;
    private String recentBill;
    private String recentBillIntroducedOn;
    private String tweet;
    private String location;
    private String watchZIPCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        cCtx = this;
        //GET VIEWS
        textViewSearchMode = (TextView) findViewById(R.id.userInput);
        textViewFoundBy = (TextView) findViewById(R.id.textViewFoundBy);


        //GET INTENT
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        zipcode = extras.getString("ZIPCODE");
        longitude = extras.getString("LONGITUDE");
        latitude = extras.getString("LATITUDE");
        watchZIPCODE = extras.getString("WATCH_ZIP");

        if(zipcode.equals("-1")){
            textViewSearchMode.setText("Current Location");
            selectedLocation = "Current Location";
            //BUILD URL
            stringUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude="+ longitude +"&apikey=" + SUNLIGHT_API_KEY;
        }else{
            textViewSearchMode.setText("ZIP: "+ zipcode);
            selectedLocation = zipcode;
            //BUILD URL
            stringUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zipcode + "&apikey=" + SUNLIGHT_API_KEY;

        }

        // Uses AsyncTask to create a task away from the main UI thread. This task takes a
        // URL string and uses it to create an HttpUrlConnection. Once the connection
        // has been established, the AsyncTask downloads the contents of the webpage as
        // an InputStream. Finally, the InputStream is converted into a string, which is
        // displayed in the UI by the AsyncTask's onPostExecute method.
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
                if(response == null) {
                    response = "THERE WAS AN ERROR";
                }
                Log.i("INFO", response);


                //Array For myAdapter & JSON string for phone to watch service
                ArrayList<Representative> reps = new ArrayList<Representative>();
                //For Watch Intent, We send LOCATION, NUM_REPRESENTATIVES, and REPRESENTATIVE1, REPRESENTATIVE2 ...
                Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

                try {
                    JSONObject jasonObject = new JSONObject(response);
                    JSONArray jsonArray = jasonObject.optJSONArray("results");
                    Representative representative;
                    for(int i=0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        bid = object.getString("bioguide_id");
                        first_name = object.getString("first_name");
                        last_name = object.getString("last_name");
                        party = object.getString("party");
                        title = object.getString("title");
                        email = object.getString("oc_email");
                        website = object.getString("website");
                        endTerm = object.getString("term_end");
                        committee = "";
                        recentBill = "";
                        recentBillIntroducedOn = "";
                        twitterId = object.getString("twitter_id");
                        tweet = "";
                        location = selectedLocation;
                        switch(party){
                            case "D":
                                party = "Democrat";
                                break;
                            case "R":
                                party = "Republican";
                                break;
                            default:
                                party = "Independent";
                        }
                        name = title + ". " + first_name + " " + last_name;

                        representative = new Representative(bid,first_name,last_name,party,title,email,website,endTerm,committee,recentBill,recentBillIntroducedOn, twitterId, tweet,location);
                        reps.add(representative);
                    }


                    //For multiple representatives
                    Gson gson = new Gson();
                    String jsonStringArray  = gson.toJson(reps);
                    watchIntent.putExtra("JSON_STRING_ARRAY", jsonStringArray);

                } catch (JSONException e) {
                    // Appropriate error handling code
                    System.out.println("================");
                    System.out.println("JSON OBJECT ERROR!!");
                    System.out.println("================");
                }

                //FOR ADAPTER
                repListView = (ListView) findViewById(R.id.listViewRepresentatives);
                repAdapter = new MyAdapter(getBaseContext(), reps);
                repListView.setAdapter(repAdapter);
                repListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent detail = new Intent(cCtx, DetailActivity.class);
                        Bundle extras = new Bundle();
                        Representative rep = (Representative) adapterView.getItemAtPosition(position);
                        bid = rep.bid;
                        //twitterId = rep.twitterId;

                        extras.putString("BID", bid);
                        extras.putString("NAME", name);
                        extras.putString("PARTY", party);
                        extras.putString("EMAIL", email);
                        extras.putString("WEBSITE", website);
                        extras.putString("END_TERM", endTerm);
                        extras.putString("TWITTER_ID", twitterId);

                        detail.putExtras(extras);
                        startActivity(detail);
                    }
                });
                //For Watch
                startService(watchIntent);



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
                    }
                    finally{
                        conn.disconnect();
                    }
                }
                catch(Exception e) {
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


        //voteView Button
        Button voteViewButton = (Button) findViewById(R.id.voteViewButton);

        voteViewButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(cCtx, PresidentialVoteActivity.class);
                        intent.putExtra("ZIPCODE", zipcode);
                        intent.putExtra("LONGITUDE",longitude);
                        intent.putExtra("LATITUDE",latitude);
                        startActivity(intent);

                    }
                }
        );

        //mainView Button
        Button buttonMain = (Button) findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(mainIntent);
                    }
                }
        );

    }
}
