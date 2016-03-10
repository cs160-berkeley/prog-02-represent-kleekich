package com.example.kangsik.represent;

/**
 * Created by Kangsik on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CongressionalActivity extends Activity {
    private Context cCtx;
    private String selectedLocation;
    private ArrayAdapter repAdapter;
    private ListView repListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        cCtx = this;

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



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String userInputMessage = extras.getString("userInputMessage");

        //Top TextView changes and Location info by userinput for selecting representatives in the list
        //Also change adapter data according to user input.

        repListView = (ListView) findViewById(R.id.listViewRepresentatives);
        TextView tv = (TextView) findViewById(R.id.userInput);
        if(userInputMessage.equals("Current Location")){
            selectedLocation = "94704";
            tv.setText("Current Location");
            //for my adapter
            repAdapter = new MyAdapter(this, reps);
            repListView.setAdapter(repAdapter);

        }
        else{
            selectedLocation = userInputMessage;
            tv.setText("ZIP: "+userInputMessage);
            repAdapter = new MyAdapter(this, sens);
            repListView.setAdapter(repAdapter);

        }





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
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(mainIntent);
                    }
                }
        );



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
                String endDate = rep.endDate;
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





        //arrayListRepresentatatives = new ArrayList<String>();
        //arrayAdapterRepresentatives = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListRepresentatives);
        //ListView listViewRepresentatives = (ListView) findViewById(R.id.listViewRepresentatives);
        //listViewRepresentatives.setAdapter(arrayAdapterRepresentatives);
    }
}
