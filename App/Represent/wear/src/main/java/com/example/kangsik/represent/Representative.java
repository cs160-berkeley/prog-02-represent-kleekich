package com.example.kangsik.represent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kangsik on 3/2/16.
 */
public class Representative implements Parcelable {
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

    public Representative(String id, String fn, String ln, String pa, String tit, String em, String w, String en, String c, String b, String bi, String tId, String t){
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bid);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.party);
        dest.writeString(this.title);
        dest.writeString(this.email);
        dest.writeString(this.website);
        dest.writeString(this.endTerm);
        dest.writeString(this.committee);
        dest.writeString(this.recentBill);
        dest.writeString(this.recentBillIntroducedOn);
        dest.writeString(this.twitterId);
        dest.writeString(this.tweet);
    }

    protected Representative(Parcel in) {
        this.bid = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.party = in.readString();
        this.title = in.readString();
        this.email = in.readString();
        this.website = in.readString();
        this.endTerm = in.readString();
        this.committee = in.readString();
        this.recentBill = in.readString();
        this.recentBillIntroducedOn = in.readString();
        this.twitterId = in.readString();
        this.tweet = in.readString();
    }

    public static final Parcelable.Creator<Representative> CREATOR = new Parcelable.Creator<Representative>() {
        public Representative createFromParcel(Parcel source) {
            return new Representative(source);
        }

        public Representative[] newArray(int size) {
            return new Representative[size];
        }
    };
}
