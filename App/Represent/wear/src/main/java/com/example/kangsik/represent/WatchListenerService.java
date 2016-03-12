package com.example.kangsik.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Kangsik on 3/3/16.
 */

public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String PHONE_TO_WATCH = "/PHONE_TO_WATCH";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //use the 'path' field in sendmessage to differentiate use cases
        if( messageEvent.getPath().equalsIgnoreCase( PHONE_TO_WATCH ) ) {
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

            String jsonStringArray = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            intent.putExtra("JSON_STRING_ARRAY", jsonStringArray);
            Log.d("T", "about to start watch MainActivity with JSON_STRING_ARRAY");
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}