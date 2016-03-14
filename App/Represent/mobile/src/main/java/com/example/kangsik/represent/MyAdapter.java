package com.example.kangsik.represent;

/**
 * Created by Kangsik on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.LruCache;



import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;



import retrofit.http.GET;
import retrofit.http.Query;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends ArrayAdapter<Representative> {
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Activity context;
    private final List<Representative> representatives;
    public AppSession guestAppSession;
    private GoogleApiClient mApiClient;

    public MyAdapter(Activity context, ArrayList<Representative> representatives) {
        super(context,R.layout.row_layout, representatives);
        //super(context, R.layout.congressional_list_view_cell, legislators);
        this.context= context;
        this.representatives=representatives;

        //For images
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout, parent, false);
        //View rowView=inflater.inflate(R.layout.congressional_list_view_cell, null, true);

        final Representative rep = getItem(position);

        //ImageView in row
        ImageView pictureView = (ImageView) theView.findViewById(R.id.imageViewPicture);
        //pictureView.setImageResource(R.drawable.slide);

        //textView in row
        TextView nameTextView = (TextView) theView.findViewById(R.id.nameTextView);
        TextView partyTextView = (TextView) theView.findViewById(R.id.partyTextView);
        TextView emailTextView = (TextView) theView.findViewById(R.id.emailTextView);
        TextView websiteTextView = (TextView) theView.findViewById(R.id.websiteTextView);


        String name = rep.title +". "+ rep.firstName + " "+ rep.lastName;
        String party = rep.party;
        String email = rep.email;
        String website = rep.website;


        nameTextView.setText(name);
        partyTextView.setText(party);
        emailTextView.setText(email);
        websiteTextView.setText(website);


        // update Tweet view if twittter ID is not null
        /*
        if (rep.twitterId != null) {
            updateTweetViewAndImageView(rowView, rep.twitterId);
        } else {
            // if no twitter account, tweet view
            ImageView twitterLogoView = (ImageView) rowView.findViewById(R.id.twitterLogo);
            twitterLogoView.setImageDrawable(null);
            rowView.findViewById(R.id.tweetView).setVisibility(View.GONE);
        }

        */
        return theView;

    }

    public void updateTweetViewAndImageView(View rowView, String twitterId) {
        final TextView tweetTextView = (TextView) rowView.findViewById(R.id.tweetTextView);
        //final CircleNetworkImageView imageView = (CircleNetworkImageView) rowView.findViewById(R.id.imageViewPicture);
        //ImageView in row
        //ImageView pictureView = (ImageView) rowView.findViewById(R.id.imageViewPicture);
        new UsersTwitterApiClient(guestAppSession).getUsersService().show(null, twitterId, true,
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        // extract tweet text
                        String tweet = result.data.status.text;
                        String imageURL = parseTwitterImageURLForOriginalImage(result.data.profileImageUrl);

                        tweetTextView.setText(tweet);

                        //extract profile photo
                        //imageView.setImageUrl(imageURL,mImageLoader);
                    }
                    @Override
                    public void failure(TwitterException exception) {
                    }
                });
    }

    public void updateImageView(final ImageView imageView, final String url) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    imageView.setImageDrawable(d);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();

    }

    private String parseTwitterImageURLForOriginalImage(String url) {
        String regexExprMatch = "_[a-z]+?(?=\\.)";
        String newUrl = url.replaceAll(regexExprMatch, "");
        return newUrl;
    }

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

}
