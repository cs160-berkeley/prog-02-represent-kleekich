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

            String bid = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            System.out.println("======IN PHONE_LISTENER====");
            System.out.println(bid);
            System.out.println("======IN PHONE_LISTENER====");
            Intent intent = new Intent(getBaseContext(), DetailActivity.class );
            Bundle extras = new Bundle();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

            extras.putString("BID", bid);
            intent.putExtras(extras);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase(WATCH_TO_CONGRESSIONAL)){
            Intent congressionalIntent = new Intent(getBaseContext(), CongressionalActivity.class );
            Bundle extras = new Bundle();
            congressionalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            String location = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            congressionalIntent.putExtra("RANDOM_LOCATION", location);
            congressionalIntent.putExtra("FROM_WATCH_SERVICE", true);

            startActivity(congressionalIntent);
        }

    }
}

