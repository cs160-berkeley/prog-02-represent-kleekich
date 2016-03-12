package com.example.kangsik.represent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kangsik on 3/2/16.
 */
public class Representative{
    public String bid;
    public String firstName;
    public String lastName;
    public String party;
    public String title;
    public String email;
    public String website;
    public String endTerm;
    public String committee;
    public String recentBill;
    public String recentBillIntroducedOn;
    public String twitterId;
    public String tweet;
    public String location;

    public Representative(String id, String fn, String ln, String pa, String tit, String em, String w, String en, String c, String b, String bi, String tId, String t, String l){
        bid = id;
        firstName = fn;
        lastName = ln;
        party = pa;
        title = tit;
        email = em;
        website = w;
        endTerm = en;
        committee = c;
        recentBill = b;
        recentBillIntroducedOn = bi;
        twitterId = tId;
        tweet = t;
        location = l;
    }


}
