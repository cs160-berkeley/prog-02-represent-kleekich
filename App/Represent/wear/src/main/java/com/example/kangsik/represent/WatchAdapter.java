package com.example.kangsik.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by Kangsik on 3/3/16.
 */
public class WatchAdapter extends FragmentGridPagerAdapter{
    Representative[][] mData;

    public WatchAdapter(FragmentManager fm, Representative[][] data) {
        super(fm);
        mData = data;
    }

    @Override
    public Fragment getFragment(int row, int column) {
        Representative rep = mData[row][column];
        CongressionalFragment cardFragment = CongressionalFragment.create(rep);

        CardFragment fragment = CardFragment.create(mData[row][column].firstName,"");

        return cardFragment;
    }



    @Override
    public int getRowCount() {
        return mData.length;
    }

    @Override
    public int getColumnCount(int row) {
        return mData[row].length;
    }
}

