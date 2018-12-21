package com.shivsau.motiondetect;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;

public class MainScreen extends FragmentActivity {

	 	ViewPager Tab;
	    TabPagerAdapter TabAdapter;
	    ActionBar actionBar;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        setTheme(R.style.AppBaseTheme);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
			boolean firstRun = p.getBoolean("firstRun", true);
			p.edit().putBoolean("firstRun", false).commit();
			if(firstRun)
			{
			Intent i = new Intent(this, AllTimeService.class);
			startService(i);
			Intent i2 = new Intent(this, HowToMain.class);
			startActivity(i2);
			}
	        
	        
	        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
	        
	        
	        Tab = (ViewPager)findViewById(R.id.pager);
	        
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
	        SpannableString titlestr= new SpannableString("M o t i o n A p e");
	        titlestr.setSpan(new com.shivsau.motiondetect.TypefaceSpan(this, "Action_Man.ttf"), 0, titlestr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        actionBar.setTitle(titlestr);
	        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9900")));
	        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#e56100")));
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        
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
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.song_tab).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.main_fr).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.notepad).setTabListener(tabListener));
	    if((getIntent().getCharExtra("fromNotif", 'n'))=='n')
	    Tab.setCurrentItem(1);
	    }
	 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.settings_menu:
			Intent i = new Intent(this, SettingsMain.class);
			startActivity(i);
			
			return true;
		case R.id.info_menu:
			Intent info = new Intent(this, HowToMain.class);
			startActivity(info);
			
			return true;
			default:
			return super.onOptionsItemSelected(item);
		}
	}

	
}
