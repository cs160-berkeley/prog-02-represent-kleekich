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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

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
                recentBillTextView.setText("Recent Bill: "+recentBill+" ("+recentBillIntroducedOn+")");
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



        //TWITTER
        // MAYBE GOING INSIDE POST_EXECUTE
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                AppSession guestAppSession = result.data;
                new UsersTwitterApiClient(guestAppSession).getUsersService().show(null, twitterId, true,
                        new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                // extract tweet text
                                String tweet = result.data.status.text;
                                System.out.println(tweet);
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
        /*
        final LinearLayout myLayout
                = (LinearLayout) findViewById(R.id.my_tweet_layout);

        final List<Long> tweetIds = Arrays.asList(Long.toLong(twitterId);
        TweetUtils.loadTweets(tweetIds, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                for (Tweet tweet : result.data) {
                    myLayout.addView(new TweetView(EmbeddedTweetsActivity.this, tweet));
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast.makeText(...).show();
            }
        });
        */
        /*
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        final StatusesService statusesService = twitterApiClient.getStatusesService();

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, "elonmusk", 10, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        for (Tweet tweet : listResult.data) {
                            Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        statusesService.show(524971209851543553L, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                //Do something with result, which provides a Tweet inside of result.data
            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });
        */
        /*
        statusesService.show(524971209851543553L, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                //Do something with result, which provides a Tweet inside of result.data

            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });

    */


        /*
        class MyTwitterApiClient extends TwitterApiClient {
            public MyTwitterApiClient(TwitterSession session) {
                super(session);
            }


            public CustomService getCustomService() {
                return getService(CustomService.class);
            }
        }

        // example users/show service endpoint
        interface CustomService {
            @GET("/1.1/users/show.json")
            void show(@Query("user_id") long id, Callback<User> cb);
        }
*





            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });



        class MyTwitterApiClient extends TwitterApiClient {
            public MyTwitterApiClient(TwitterSession session) {
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

        new MyTwitterApiClient(session).getUsersService().show(12L, null, true,
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        Log.d("twittercommunity", "user's profile url is "
                                + result.data.profileImageUrlHttps);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("twittercommunity", "exception is " + exception);
                    }
                });


        TwitterSession session =
                Twitter.getSessionManager().getActiveSession();
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {


                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        twitterImage = user.profileImageUrl;

                    }

                    @Override
                    public void failure(TwitterException e) {

                    }

                });

*/
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
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

        imageView.setImageResource(R.drawable.slide);







    }

}
