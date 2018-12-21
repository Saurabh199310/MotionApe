package com.shivsau.motiondetect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter2 extends FragmentStatePagerAdapter {
	public TabPagerAdapter2(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public Fragment getItem(int i) {
        switch (i) {
        case 0:
            return new AppSettings();
        case 1:
            return new CheckSensor();
        }
        return null;
        
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
    }

}
