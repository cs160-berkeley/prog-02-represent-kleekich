package com.example.kangsik.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

    public class WatchToPhoneCongressionalService extends Service implements GoogleApiClient.ConnectionCallbacks {
        private static final String TAG = "myMessage";
        private GoogleApiClient mWatchApiClient;
        private List<Node> nodes = new ArrayList<>();
        String location = "";
        @Override
        public void onCreate() {
            super.onCreate();
            //initialize the googleAPIClient for message passing
            mWatchApiClient = new GoogleApiClient.Builder( this )
                    .addApi( Wearable.API )
                    .addConnectionCallbacks(this)
                    .build();
            //and actually connect it

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mWatchApiClient.disconnect();
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
        public void onConnected(Bundle bundle) {

        }
        @Override //we need this to implement GoogleApiClient.ConnectionsCallback
        public void onConnectionSuspended(int i) {}

        private void sendMessage(final String path, final String text ) {
            new Thread( new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mWatchApiClient ).await();
                    for (Node node : nodes.getNodes()) {
                        Wearable.MessageApi.sendMessage(
                                mWatchApiClient, node.getId(), path, text.getBytes());
                    }

                }
            }).start();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Bundle extras =intent.getExtras();
            //location = extras.getString("JSON_STRING_LOCATION");
            Boolean shake= extras.getBoolean("SHAKE");
            System.out.println("SHACK SHACK");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mWatchApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    sendMessage("/WATCH_TO_CONGRESSIONAL", "SHAKE");

                }
            }).start();

            return 0;
        }


    }


