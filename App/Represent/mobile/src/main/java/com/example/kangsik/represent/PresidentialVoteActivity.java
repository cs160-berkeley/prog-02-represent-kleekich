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
        //dummy data
    /*
        HashMap<String, HashMap<String, String>> dummyHashMap = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> dummy11111 = new HashMap<String, String>();
        HashMap<String, String> dummy94704 = new HashMap<String, String>();
        dummy11111.put("state", "California");
        dummy11111.put("district", "11th");
        dummy11111.put("personA", "Obama");
        dummy11111.put("personB", "Romney");
        dummy11111.put("percentA", "32");
        dummy11111.put("percentB", "28");
        dummy94704.put("state", "Alaska");
        dummy94704.put("district", "Askimo");
        dummy94704.put("personA", "Joey");
        dummy94704.put("personB", "Michael");
        dummy94704.put("percentA", "52");
        dummy94704.put("percentB", "21");


        dummyHashMap.put("11111", dummy11111);
        dummyHashMap.put("94704", dummy94704);

*/





        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        String selectedLocation = extras.getString("selectedLocation");


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
