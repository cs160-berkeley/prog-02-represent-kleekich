package com.example.kangsik.represent;

/**
 * Created by Kangsik on 3/2/16.
 */
public class Representative {
    public String firstName;
    public String lastName;
    public String party;
    public String position;
    public String email;
    public String website;
    public String tweet;
    public String zip;
    public String endDate;
    public String committee;
    public String recentBill;

    public Representative(String fn, String ln, String pa, String po, String em, String w, String t, String z, String en, String c, String r){
        firstName = fn;
        lastName = ln;
        party = pa;
        position = po;
        email = em;
        website = w;
        tweet = t;
        zip = z;
        endDate = en;
        committee = c;
        recentBill = r;

    }

}
