package com.example.kangsik.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Kangsik on 3/3/16.
 */
public class PresidentialVoteActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential_vote);

        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        String selectedLocation = extras.getString("LOCATION");


        //Get views;

        TextView textViewPersonA = (TextView) findViewById(R.id.textViewPersonA);
        TextView textViewPersonB = (TextView) findViewById(R.id.textViewPersonB );
        TextView textViewPercentA = (TextView) findViewById(R.id.textViewPercentA);
        TextView textViewPercentB = (TextView) findViewById(R.id.textViewPercentB);
        TextView textViewState = (TextView) findViewById(R.id.textViewState);
        TextView textViewDistrict = (TextView) findViewById(R.id.textViewDistrict);

        if(selectedLocation.equals("11111")){

            textViewPersonA.setText("Obama");
            textViewPersonB.setText("Romney");
            textViewPercentA.setText("36%");
            textViewPercentB.setText("28%");
            textViewState.setText("State: California");
            textViewDistrict.setText("District: 11th");

        }else if(selectedLocation.equals("94704")) {
            textViewPersonA.setText("Joey");
            textViewPersonB.setText("Michael");
            textViewPercentA.setText("52%");
            textViewPercentB.setText("24%");
            textViewState.setText("State: Alaska");
            textViewDistrict.setText("District: Eskimo");


        }


    };

}
