package com.example.kangsik.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);




        //Back Button
        //mainView Button/Back Button
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
        Button buttonCongressional = (Button) findViewById(R.id.buttonCongressional);
        buttonCongressional.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent congressionalIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                        startActivity(congressionalIntent);
                    }
                }
        );


        //GET Passed Data
        Intent intent  = getIntent();
        Bundle extras = intent.getExtras();

        //Get Views


        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView partyTextView = (TextView) findViewById(R.id.partyTextView);
        TextView endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        TextView committeeTextView = (TextView) findViewById(R.id.committeeTextView);
        TextView recentBillTextView = (TextView) findViewById(R.id.recentBillTextView);
        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
        TextView websiteTextView = (TextView) findViewById(R.id.websiteTextView);
        TextView tweetTextView = (TextView) findViewById(R.id.tweetTextView);


        //From Watch Service






            imageView.setImageResource(R.drawable.slide);
            nameTextView.setText(extras.getString("name"));
            partyTextView.setText("Party: ".concat(extras.getString("party")));
            emailTextView.setText("Email: ".concat(extras.getString("email")));
            websiteTextView.setText("Website: ".concat(extras.getString("website")));
            tweetTextView.setText("Last Tweet: ".concat(extras.getString("tweet")));
            endDateTextView.setText("End Date of Term: ".concat(extras.getString("endDate")));
            committeeTextView.setText("Committee: ".concat(extras.getString("committee")));
            recentBillTextView.setText("Recent Bill: ".concat(extras.getString("recentBill")));

    }

}
