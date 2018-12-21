package com.shivsau.motiondetect;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class SettingsMain extends FragmentActivity {

	 	ViewPager Tab;
	    TabPagerAdapter2 TabAdapter;
	    ActionBar actionBar;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	setTheme(R.style.AppBaseTheme);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.settings_main);
	        
	        TabAdapter = new TabPagerAdapter2(getSupportFragmentManager());
	        
	        
	        Tab = (ViewPager)findViewById(R.id.pager2);
	        
	        Tab.setOnPageChangeListener(
	                new ViewPager.SimpleOnPageChangeListener() {
	                    @Override
	                    public void onPageSelected(int position) {
	                       
	                        actionBar = getActionBar();
	                        actionBar.setSelectedNavigationItem(position);                    }
	                });
	        
	        Tab.setAdapter(TabAdapter);
	        
	        actionBar = getActionBar();
	        actionBar.setIcon(android.R.color.transparent);
	        actionBar.setTitle("Settings");
	        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9900")));
	        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#e56100")));
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
	 

		@Override
		public void onTabSelected(android.app.ActionBar.Tab tab,
				FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Tab.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(android.app.ActionBar.Tab tab,
				FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabReselected(android.app.ActionBar.Tab tab,
				FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}};
	    actionBar.addTab(actionBar.newTab().setText("Service").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("Senstivity").setTabListener(tabListener));
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