package com.example.kangsik.represent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Kangsik on 3/3/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String BART = "/Bart";
    private static final String HOMER = "/Homer";




    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());

        Representative r1 = new Representative("Bart", "Simpson", "Democrat", "Senator", "bart@gmail.com", "https://www.bart.com", "I love you!" ,"94704","03/16/2017","The Drinking Committee","Safe Drinking Act (01/16/ 2016)" );
        Representative r3 = new Representative("Lisa", "Simpson", "Independent", "Representative", "lisa@gmail.com", "https://www.lisa.com", "I hate you" ,"94704","07/06/2017", "The Skateboard Committee", "Bill: Safe Boarding Act(02/17/ 2016)");
        Representative r2 = new Representative("Homer", "Simpson", "Republican", "Senator", "homer@gmail.com", "https://www.homer.com", "I love you, too!" ,"94704","03/16/2017","The Drinking Committee","Safe Drinking Act (01/16/ 2016)");
        Representative r4 = new Representative("Maggie", "Simpson", "Independent", "Senator", "meggie@gmail.com", "https://www.meggie.com", "dada" ,"11111", "07/06/2017", "The Skateboard Committee", "Bill: Safe Boarding Act(02/17/ 2016)");

        if( messageEvent.getPath().equalsIgnoreCase(BART) ) {

            Representative rep = r1;

            Intent intent = new Intent(getBaseContext(), DetailActivity.class );
            Bundle extras = new Bundle();

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

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


            intent.putExtras(extras);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase(HOMER)){
            Representative rep = r2;


            Intent intent = new Intent(getBaseContext(), DetailActivity.class );
            Bundle extras = new Bundle();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

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
            intent.putExtras(extras);

            startActivity(intent);
        }else{
            Intent congressionalIntent = new Intent(getBaseContext(), CongressionalActivity.class );
            Bundle extras = new Bundle();
            congressionalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            String randomZipCode = messageEvent.getPath().substring(1);

            congressionalIntent.putExtra("userInputMessage", randomZipCode);

            startActivity(congressionalIntent);
        }

    }
}

