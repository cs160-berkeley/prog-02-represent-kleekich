package com.example.kangsik.represent;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends FragmentActivity {

    String selectedLocation = "12345";
    /* put this into your activity class */
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private SensorEventListener mSensorListener;

    private void reset() {
        mAccel = 0f;
        mAccelCurrent = 0f;
        mAccelLast= 0f;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);


    private TextView textViewLocation;
    private TextView textViewName;
    private TextView textViewPosition;
    private TextView textViewParty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSensorListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent se) {
                float x = se.values[0];
                float y = se.values[1];
                float z = se.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta; // perform low-cut filter

                if (mAccel > 12) {
                    Random r = new Random();
                    int randomZipCode = r.nextInt(100000 - 10000) + 10000;
                    System.out.println(randomZipCode);

                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneCongressionalService.class);
                    sendIntent.putExtra("LOCATION", Integer.toString(randomZipCode));
                    getBaseContext().startService(sendIntent);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("LOCATION", Integer.toString(randomZipCode));
                    startActivity(intent);
                    reset();
                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        //get Location

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        selectedLocation = extras.getString("LOCATION");

        final DotsPageIndicator mPageIndicator;
        final GridViewPager mViewPager;

        final String[][] data = {
                { "Row 0, Col 0", "Row 0, Col 1", "Row 0, Col 2" },

        };

        //dummy data


        Representative r1 = new Representative("Bart", "Simpson", "Democrat", "Senator", "bart@gmail.com", "https://www.bart.com", "I love you!" ,"94704","03/16/2017","The Drinking Committee","Safe Drinking Act (01/16/ 2016)" );
        Representative r2 = new Representative("Homer", "Simpson", "Republican", "Senator", "homer@gmail.com", "https://www.homer.com", "I love you, too!" ,"94704","03/16/2017","The Drinking Committee","Safe Drinking Act (01/16/ 2016)");
        Representative r3 = new Representative("Lisa", "Simpson", "Independent", "Representative", "lisa@gmail.com", "https://www.lisa.com", "I hate you" ,"94704","07/06/2017", "The Skateboard Committee", "Bill: Safe Boarding Act(02/17/ 2016)");
        Representative r4 = new Representative("Maggie", "Simpson", "Independent", "Senator", "meggie@gmail.com", "https://www.meggie.com", "dada" ,"11111", "07/06/2017", "The Skateboard Committee", "Bill: Safe Boarding Act(02/17/ 2016)");

        Representative[][] representatives = {{r1,r2,r3}};
        if(selectedLocation.equals("11111")){
            representatives[0][0] = r3;
            representatives[0][1] = r2;
            representatives[0][2] = r1;
        }else if(selectedLocation.equals("94704")){
            representatives[0][0] = r1;
            representatives[0][1] = r2;
            representatives[0][2] = r3;
        }


        // Get UI references
        mPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        mViewPager = (GridViewPager) findViewById(R.id.pager);

        // Assigns an adapter to provide the content for this pager
        mViewPager.setAdapter(new WatchAdapter(getFragmentManager(), representatives));
        mPageIndicator.setPager(mViewPager);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        launchPresidentialVoteActivity(v);
                        return true;
                }
                return false;
            }
        });

    }
    public void launchPresidentialVoteActivity(View v) {
        Intent intent = new Intent(getBaseContext(), PresidentialVoteActivity.class);
        intent.putExtra("LOCATION", selectedLocation);

        startActivity(intent);
    }

/*
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }
11
    private void updateDisplay() {
        if (isAmbient()) {

            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }
    */
}
