package com.example.kangsik.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Kangsik on 3/3/16.
 */
public class WatchAdapter extends FragmentGridPagerAdapter{

    ArrayList<Representative> representatives;

    public WatchAdapter(FragmentManager fm, ArrayList<Representative> data) {
        super(fm);

        this.representatives = data;
    }

    @Override
    public Fragment getFragment(int row, int column) {
        Representative rep = representatives.get(column);

        CongressionalFragment cardFragment = CongressionalFragment.create(rep);

        CardFragment fragment = CardFragment.create(rep.firstName,"");

        return cardFragment;
    }



    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return representatives.size();
    }
}

