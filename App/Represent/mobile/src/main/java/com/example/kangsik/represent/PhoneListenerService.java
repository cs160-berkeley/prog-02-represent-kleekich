package com.example.kangsik.represent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

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
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        Intent intent;

        if( messageEvent.getPath().equalsIgnoreCase(WATCH_TO_DETAIL) ) {
            intent = new Intent(getBaseContext(), DetailActivity.class);
            String bid = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            System.out.println("======IN PHONE_LISTENER_DETAIL====");
            System.out.println(bid);
            System.out.println("======IN PHONE_LISTENER_DETAIL====");
            Bundle extras = new Bundle();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

            extras.putString("BID", bid);
            intent.putExtras(extras);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase(WATCH_TO_CONGRESSIONAL)){
            intent = new Intent(getBaseContext(), CongressionalActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            /*
            String location = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            congressionalIntent.putExtra("RANDOM_LOCATION", location);
            */
            System.out.println("======IN PHONE_LISTENER_CONGRESSIONAL====");

            System.out.println("======IN PHONE_LISTENER_CONGRESSIONAL====");
            intent.putExtra("FROM_WATCH_SERVICE", true);

            startActivity(intent);
        }

    }
}

