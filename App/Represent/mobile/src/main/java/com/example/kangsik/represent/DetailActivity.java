package com.example.kangsik.represent;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;




import retrofit.http.GET;
import retrofit.http.Query;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;




public class DetailActivity extends Activity {
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    //VIEWS
    ImageView imageView;
    TextView nameTextView;
    TextView partyTextView;
    TextView endDateTextView;
    TextView committeeTextView;
    TextView recentBillTextView;
    TextView emailTextView;
    TextView websiteTextView;
    TextView tweetTextView;
    private Button buttonMain;
    private Button buttonCongressional;

    //URL
    private String API_KEY = "45a993c1ef534c45b4b15bfc6ead5422";
    private String stringUrlCommittee;
    private String stringUrlBill;
    private String stringUrlRepresentative;

    //FOR THE REPRESENTATIVE
    private String bid;
    private String lastName;
    private String firstName;
    private String title;
    private String name;
    private String party;
    private String endTerm;
    private String committee;
    private String recentBill;
    private String recentBillIntroducedOn;
    private String email;
    private String website;
    private String twitterId;
    private static final String TWITTER_KEY = "4Jv7s0n2tUqSdM4FsiQ2aZGuw";
    private static final String TWITTER_SECRET = "gw2MY2JZHdlpS18sLMsV1dhYKPOGI4B8bra7PGET8Fm7wKxlV8";

    class UsersTwitterApiClient extends TwitterApiClient {
        public UsersTwitterApiClient(AppSession session) {
            super(session);
        }
        public UsersService getUsersService() {
            return getService(UsersService.class);
        }
    }
    interface UsersService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long userId,
                  @Query("screen_name") String screenName,
                  @Query("include_entities") Boolean includeEntities,
                  Callback<User> cb);
    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(AppSession session) {
            super(session);
        }

        /**
         * Provide CustomService with defined endpoints
         */
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }
    }

    // example users/show service endpoint
    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long id,
                  @Query("screen_name") String twitterStringID,
                  @Query("include_entities") Boolean includeEntities,
                  Callback<User> cb);
    }

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
                if(response.contains("bioguide_id")){
                    try {
                        firstName = object.getString("first_name");
                        lastName = object.getString("last_name");
                        title = object.getString("title");
                        name = title + ". " + firstName + " " + lastName;
                        party = object.getString("party");
                        switch (party) {
                            case "D":
                                party = "Democrat";
                                break;
                            case "R":
                                party = "Republican";
                                break;
                            default:
                                party = "Independent";
                        }
                        endTerm = object.getString("term_end");
                        email = object.getString("oc_email");
                        website = object.getString("website");
                        twitterId = object.getString("twitter_id");




                    }catch(JSONException e){
                        System.out.println("============");
                        System.out.println(e);
                        System.out.println("============");
                    }


                }else if(response.contains("bill_id")){
                    try {
                        recentBill = object.getString("short_title");
                    }catch(JSONException e){
                        recentBill = object.getString("official_title");
                    }
                    recentBillIntroducedOn = object.getString("introduced_on");

                }else{
                    committee = object.getString("name");
                }
                nameTextView.setText(name);
                partyTextView.setText("Party: ".concat(party));
                emailTextView.setText("Email: ".concat(email));
                websiteTextView.setText("Website: ".concat(website));
                endDateTextView.setText("End Date of Term: ".concat(endTerm));
                committeeTextView.setText("Committee: "+committee);
                recentBillTextView.setText("Recent Bill: " + recentBill + " (" + recentBillIntroducedOn + ")");


            } catch (JSONException e) {
                // Appropriate error handling code
                System.out.println(e.getMessage());

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_detail);
        //GET Passed Data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //If it is from watch
        bid = extras.getString("BID");
        //BUILD URL
        stringUrlRepresentative = "http://congress.api.sunlightfoundation.com/legislators?bioguide_id="+bid+"&apikey="+API_KEY;
        stringUrlCommittee = "http://congress.api.sunlightfoundation.com/committees?member_ids=" + bid + "&apikey=" + API_KEY;
        stringUrlBill = "http://congress.api.sunlightfoundation.com/bills?sponsor_id="+ bid +"&apikey="+API_KEY;
        //For Image
        //For images
        mRequestQueue = Volley.newRequestQueue(getBaseContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrlRepresentative);
            new DownloadWebpageTask().execute(stringUrlCommittee);
            new DownloadWebpageTask().execute(stringUrlBill);
        } else {
            System.out.println("==================================");
            System.out.println("No network connection available.");
            System.out.println("==================================");
        }

        //TWITTER
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                AppSession guestAppSession = result.data;
                final NetworkImageView profImage = (NetworkImageView) findViewById(R.id.imageViewPicture);
                new UsersTwitterApiClient(guestAppSession).getUsersService().show(null, twitterId, true,
                        new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                // extract tweet text
                                String tweet = result.data.status.text;
                                tweetTextView.setText(tweet);
                                String imageURL = parseTwitterImageURLForOriginalImage(result.data.profileImageUrl);
                                //extract profile photo
                                profImage.setImageUrl(imageURL, mImageLoader);
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                System.out.println(exception);
                            }
                        });
            }

            @Override
            public void failure(TwitterException e) {
                System.out.println(e);
            }
        });


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


        //Get Views
        imageView = (ImageView) findViewById(R.id.imageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        partyTextView = (TextView) findViewById(R.id.partyTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        committeeTextView = (TextView) findViewById(R.id.committeeTextView);
        recentBillTextView = (TextView) findViewById(R.id.recentBillTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        websiteTextView = (TextView) findViewById(R.id.websiteTextView);
        tweetTextView = (TextView) findViewById(R.id.tweetTextView);

    }
    private String parseTwitterImageURLForOriginalImage(String url) {
        String regexExprMatch = "_[a-z]+?(?=\\.)";
        String newUrl = url.replaceAll(regexExprMatch, "");
        return newUrl;
    }

}
