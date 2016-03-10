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
    private Boolean useCurrentLocation;
    private String stringUrl;

    private final static String SUNLIGHT_API_KEY = "45a993c1ef534c45b4b15bfc6ead5422";


    private TextView textViewSearchMode;
    private TextView textViewFoundBy;
    private TextView textViewResponse;

    //FOR REPRESENTATIVE
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        cCtx = this;
        //GET VIEWS
        textViewSearchMode = (TextView) findViewById(R.id.userInput);
        textViewFoundBy = (TextView) findViewById(R.id.textViewFoundBy);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);

        //GET INTENT
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        zipcode = extras.getString("ZIPCODE");
        longitude = extras.getString("LONGITUDE");
        latitude = extras.getString("LATITUDE");
        if(zipcode.equals("-1")){
            useCurrentLocation = true;
            textViewSearchMode.setText("Current Location");

            //BUILD URL
            stringUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude="+ longitude +"&apikey=" + SUNLIGHT_API_KEY;
        }else{
            useCurrentLocation = false;
            textViewSearchMode.setText("ZIP: "+ zipcode);

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
                textViewResponse.setText(response);

                //Array For myAdapter
                ArrayList<Representative> reps = new ArrayList<Representative>();

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

                        representative = new Representative(bid,first_name,last_name,party,title,email,website,endTerm,committee,recentBill,recentBillIntroducedOn, twitterId, tweet);
                        reps.add(representative);
                    }
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
                        String name = rep.firstName.concat(" ").concat(rep.lastName);
                        String party = rep.party;
                        String email = rep.email;
                        String website = rep.website;
                        String tweet = rep.tweet;
                        String endDate = rep.endTerm;
                        String committee = rep.committee;
                        String recentBill = rep.recentBill;



                        extras.putString("name", name);
                        extras.putString("party", party);
                        extras.putString("email", email);
                        extras.putString("website", website);
                        extras.putString("tweet", tweet);
                        extras.putString("endDate", endDate);
                        extras.putString("committee", committee);
                        extras.putString("recentBill", recentBill);


                        detail.putExtras(extras);

                        startActivity(detail);

                    }
                });



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
           /*
            // Reads an InputStream and converts it to a String.
            public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
                Reader reader = null;
                reader = new InputStreamReader(stream, "UTF-8");
                char[] buffer = new char[len];
                reader.read(buffer);
                return new String(buffer);
            }
            */
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



/*
        //dummy data
        ArrayList<Representative> sens = new ArrayList<Representative>();
        ArrayList<Representative> reps = new ArrayList<Representative>();

        Representative r1 = new Representative("Bart", "Simpson", "Democrat", "Senator", "bart@gmail.com", "https://www.bart.com", "I love you!" ,"94704","03/16/2017","The Drinking Committee","Safe Drinking Act (01/16/ 2016)" );
        Representative r3 = new Representative("Lisa", "Simpson", "Independent", "Representative", "lisa@gmail.com", "https://www.lisa.com", "I hate you" ,"94704","07/06/2017", "The Skateboard Committee", "Bill: Safe Boarding Act(02/17/ 2016)");
        Representative r2 = new Representative("Homer", "Simpson", "Republican", "Senator", "homer@gmail.com", "https://www.homer.com", "I love you, too!" ,"94704","03/16/2017","The Drinking Committee","Safe Drinking Act (01/16/ 2016)");
        Representative r4 = new Representative("Maggie", "Simpson", "Independent", "Senator", "meggie@gmail.com", "https://www.meggie.com", "dada" ,"11111", "07/06/2017", "The Skateboard Committee", "Bill: Safe Boarding Act(02/17/ 2016)");



        reps.add(r1);
        reps.add(r2);
        reps.add(r4);
        sens.add(r3);
        sens.add(r2);
        sens.add(r1);


        //Top TextView changes and Location info by userinput for selecting representatives in the list
        //Also change adapter data according to user input.

        repListView = (ListView) findViewById(R.id.listViewRepresentatives);


        if(useCurrentLocation){
            //selectedLocation = "94704";
            textViewSearchMode.setText("Current Location");
            //for my adapter
            repAdapter = new MyAdapter(this, reps);
            repListView.setAdapter(repAdapter);

        }else{
            //selectedLocation = userInputMessage;
            textViewSearchMode.setText("ZIP: "+zipcode);
            repAdapter = new MyAdapter(this, sens);
            repListView.setAdapter(repAdapter);

        }




*/
        //voteView Button
        Button voteViewButton = (Button) findViewById(R.id.voteViewButton);

        voteViewButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(cCtx, PresidentialVoteActivity.class);
                        intent.putExtra("selectedLocation", selectedLocation);
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









        //arrayListRepresentatatives = new ArrayList<String>();
        //arrayAdapterRepresentatives = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListRepresentatives);
        //ListView listViewRepresentatives = (ListView) findViewById(R.id.listViewRepresentatives);
        //listViewRepresentatives.setAdapter(arrayAdapterRepresentatives);
    }
}
