package com.example.kangsik.represent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.fabric.sdk.android.Fabric;

public class DetailActivity extends AppCompatActivity {
    //VIEWS
    ImageView imageView;
    TextView nameTextView;
    TextView partyTextView;
    TextView endDateTextView;
    TextView committeeTextView;
    TextView recentBillTextView;
    TextView emailTextView;
    TextView websiteTextView;
    private Button buttonMain;
    private Button buttonCongressional;

    //URL
    private String API_KEY = "45a993c1ef534c45b4b15bfc6ead5422";
    private String stringUrlCommittee;
    private String stringUrlBill;

    //FOR THE REPRESENTATIVE
    private String bid;
    private String name;
    private String party;
    private String endTerm;
    private String committee;
    private String recentBill;
    private String recentBillIntroducedOn;
    private String email;
    private String website;
    private String twitterId;
    private static final String TWITTER_KEY = "kleekich@berkeley.edu";
    private static final String TWITTER_SECRET = "Grant6312!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));



        //mainView Button/Back Button
        buttonMain = (Button) findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(mainIntent);
                    }
                }
        );
        buttonCongressional = (Button) findViewById(R.id.buttonCongressional);
        buttonCongressional.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent congressionalIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                        startActivity(congressionalIntent);
                    }
                }
        );


        //GET Passed Data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        bid = extras.getString("BID");
        name = extras.getString("NAME");
        party = extras.getString("PARTY");
        email = extras.getString("EMAIL");
        website = extras.getString("WEBSITE");
        endTerm = extras.getString("END_TERM");
        twitterId = extras.getString("TWITTER_ID");


        //QUERY
        //BUILD URL
        stringUrlCommittee = "http://congress.api.sunlightfoundation.com/committees?member_ids=" + bid + "&apikey=" + API_KEY;
        stringUrlBill = "http://congress.api.sunlightfoundation.com/bills?sponsor_id="+ bid +"&apikey="+API_KEY;

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
                if (response == null) {
                    response = "THERE WAS AN ERROR";
                }
                Log.i("INFO", response);

                try {
                    //JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject jasonObject = new JSONObject(response);
                    JSONArray jsonArray = jasonObject.optJSONArray("results");
                    JSONObject object = jsonArray.getJSONObject(0);
                    if(!response.contains("bill_id")){
                        committee = object.getString("name");
                    }else{
                        try {
                            recentBill = object.getString("short_title");
                        }catch(JSONException e){
                            System.out.println("================");
                            System.out.println(e.getMessage());
                            System.out.println("================");
                            recentBill = object.getString("official_title");
                        }
                        recentBillIntroducedOn = object.getString("introduced_on");
                    }
                    committeeTextView.setText("Committee: "+committee);
                    recentBillTextView.setText("Recent Bill: "+recentBill+" ("+recentBillIntroducedOn+")");
                } catch (JSONException e) {
                    // Appropriate error handling code
                    System.out.println("================");
                    System.out.println(e.getMessage());
                    System.out.println("================");
                }
            }
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
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrlCommittee);
            new DownloadWebpageTask().execute(stringUrlBill);
        } else {
            System.out.println("==================================");
            System.out.println("No network connection available.");
            System.out.println("==================================");
        }



        //Get Views
        imageView = (ImageView) findViewById(R.id.imageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        partyTextView = (TextView) findViewById(R.id.partyTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        committeeTextView = (TextView) findViewById(R.id.committeeTextView);
        recentBillTextView = (TextView) findViewById(R.id.recentBillTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        websiteTextView = (TextView) findViewById(R.id.websiteTextView);



        imageView.setImageResource(R.drawable.slide);
        nameTextView.setText(name);
        partyTextView.setText("Party: ".concat(party));
        emailTextView.setText("Email: ".concat(email));
        websiteTextView.setText("Website: ".concat(website));
        endDateTextView.setText("End Date of Term: ".concat(endTerm));



    }

}
