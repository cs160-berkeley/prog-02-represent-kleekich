package com.example.kangsik.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by Kangsik on 3/3/16.
 */
public class CongressionalFragment extends CardFragment {
    private static final String TAG = "myMessage";
    Representative representative;



    public CongressionalFragment() {
        super();
    }

    public static CongressionalFragment create(Representative r) {
        CongressionalFragment fragment = new CongressionalFragment();
        fragment.addRepresentative(r);
        return fragment;
    }

    public void addRepresentative(Representative r) {
        representative = r;

        Bundle args = new Bundle();
        args.putCharSequence("CardFragment_title", representative.title+". "+ representative.firstName.concat(" ").concat(representative.lastName));
        args.putCharSequence("CardFragment_text", representative.party);
        this.setArguments(args);
    }



    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateContentView(inflater, container, savedInstanceState);
        TextView title = (TextView) view.findViewById(android.support.wearable.R.id.title);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("======IN CONG_FRAG====");
                System.out.println(representative.bid);
                System.out.println("======IN CONG_FRAG====");
                Intent intent = new Intent(getActivity(), WatchToPhoneService.class);
                intent.putExtra("BID", representative.bid);
                getActivity().startService(intent);

            }
        });


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        System.out.println("action down");
                        return true;
                    }
                }
                return false;
            }

        });
        return view;
    }
}