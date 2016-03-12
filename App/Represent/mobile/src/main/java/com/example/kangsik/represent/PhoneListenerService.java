package com.example.kangsik.represent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

/**
 * Created by Kangsik on 3/3/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String WATCH_TO_DETAIL = "/WATCH_TO_DETAIL";
    private static final String WATCH_TO_CONGRESSIONAL = "/WATCH_TO_CONGRESSIONAL";





    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(WATCH_TO_DETAIL) ) {

            String stringRep = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            System.out.println("==========");
            System.out.println("stringRep: "+ stringRep);
            System.out.println("==========");
            Gson gson = new Gson();
            Representative rep = gson.fromJson(stringRep, Representative.class);


            Intent intent = new Intent(getBaseContext(), DetailActivity.class );
            Bundle extras = new Bundle();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service


            String bid = rep.bid;
            String name = rep.title + ". " + rep.firstName + " " + rep.lastName;
            String party = rep.party;
            String email = rep.email;
            String website = rep.website;
            String endDate = rep.endTerm;
            String twitterId = rep.twitterId;


            extras.putString("BID", bid);
            extras.putString("NAME", name);
            extras.putString("PARTY", party);
            extras.putString("EMAIL", email);
            extras.putString("WEBSITE", website);
            extras.putString("END_TERM", endDate);
            extras.putString("TWITTER_ID", twitterId);



            intent.putExtras(extras);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase(WATCH_TO_CONGRESSIONAL)){
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

