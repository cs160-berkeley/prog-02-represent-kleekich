package com.example.kangsik.represent;

/**
 * Created by Kangsik on 3/2/16.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import java.util.*;

public class MyAdapter extends ArrayAdapter<Representative> {

    public MyAdapter(Context context, ArrayList<Representative> values) {
        super(context,R.layout.row_layout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout, parent, false);

        Representative rep = getItem(position);

        //ImageView in row
        ImageView pictureView = (ImageView) theView.findViewById(R.id.imageViewPicture);
        pictureView.setImageResource(R.drawable.slide);

        //textView in row
        TextView nameTextView = (TextView) theView.findViewById(R.id.nameTextView);
        TextView partyTextView = (TextView) theView.findViewById(R.id.partyTextView);
        TextView emailTextView = (TextView) theView.findViewById(R.id.emailTextView);
        TextView websiteTextView = (TextView) theView.findViewById(R.id.websiteTextView);
        TextView tweetTextView = (TextView) theView.findViewById(R.id.tweetTextView);

        String name = rep.firstName.concat(" ").concat(rep.lastName);
        String party = rep.party;
        String email = rep.email;
        String website = rep.website;
        String tweet = rep.tweet;

        nameTextView.setText(name);
        partyTextView.setText(party);
        emailTextView.setText(email);
        websiteTextView.setText(website);
        tweetTextView.setText(tweet);



        return theView;

    }
}
