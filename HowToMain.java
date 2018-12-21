package com.shivsau.motiondetect;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.ImageView;

public class HowToMain extends FragmentActivity {
	
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	ImageView iv1;
	ImageView iv2;
	ImageView iv3;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		setTheme(R.style.AppBaseTheme);
		super.onCreate(arg0);
		setContentView(R.layout.how_to_main);
		iv1 = (ImageView) findViewById(R.id.pg1);
		iv2 = (ImageView) findViewById(R.id.pg2);
		iv3 = (ImageView) findViewById(R.id.pg3);
		
		ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle("How to use MotionApe");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9900")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		mPager = (ViewPager) findViewById(R.id.howToPager);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			 @Override
             public void onPageSelected(int position) {
				 if(position==0)
				 {
					iv1.setImageResource(R.drawable.selected);
					iv2.setImageResource(R.drawable.unselected);
					iv3.setImageResource(R.drawable.unselected);
				 }
				 else if(position==1)
				 {
					iv2.setImageResource(R.drawable.selected);
					iv1.setImageResource(R.drawable.unselected);
					iv3.setImageResource(R.drawable.unselected);
				 }
				 else if(position==2)
				 {
					iv3.setImageResource(R.drawable.selected);
					iv1.setImageResource(R.drawable.unselected);
					iv2.setImageResource(R.drawable.unselected);
				 }
			 }
			 });
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
	
	 private class SlidePagerAdapter extends FragmentStatePagerAdapter {
	        public SlidePagerAdapter(FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public Fragment getItem(int position) {
	            switch(position)
	            {
	            case 0:
	            	return new HowToOne();
	            case 1:
	            	return new HowToTwo();
	            case 2:
	            	return new HowToThree();
	            }
	            return null;
	        }

	        @Override
	        public int getCount() {
	            return 3;
	        }
	    }
	 
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch (item.getItemId()) {
		    case android.R.id.home:
		        NavUtils.navigateUpFromSameTask(this);
		        return true;
		    }
		    return super.onOptionsItemSelected(item);
		}

}
