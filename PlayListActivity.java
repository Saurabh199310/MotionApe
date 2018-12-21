package com.shivsau.motiondetect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlayListActivity extends Activity {

public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
MyHelper2 mh;
ListAdapter adapt;
Button main;
Button add;
ListView lv;
ArrayAdapter<Model> adapt2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);	
		ActionBar ab = getActionBar();
		ab.setIcon(android.R.color.transparent);
        ab.setTitle("Songs");
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9900")));
		ab.setDisplayHomeAsUpEnabled(true);
		
		SongsManager plm = new SongsManager(getBaseContext());
		songsList = plm.getPlayList();
		adapt2 = new MyAdapter(this, getModel());
		lv = (ListView) findViewById(R.id.list);
		lv.setAdapter(adapt2);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		main=(Button) findViewById(R.id.main);
		add = (Button) findViewById(R.id.addToList);
		main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mh =new MyHelper2(getApplicationContext());
				SQLiteDatabase db= mh.getWritableDatabase();
				db.execSQL("delete from playlist");
				
				for(int j=0;j<adapt2.getCount();j++)
				{
					Model element = adapt2.getItem(j);
					if(element.isSelected())
					{
						ContentValues cv = new ContentValues();
						cv.put("songTitle", songsList.get(j).get("songTitle"));
						cv.put("songPath",songsList.get(j).get("songPath"));
						db.insert("playlist", null, cv);
						cv.clear();
					}
				}
				db.close();
				Toast.makeText(getBaseContext(),"New songlist has been created.", Toast.LENGTH_LONG).show();
				finish();
			}
		});
			add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mh =new MyHelper2(getApplicationContext());
					SQLiteDatabase db= mh.getWritableDatabase();
					
					for(int j=0;j<adapt2.getCount();j++)
					{
						Model element = adapt2.getItem(j);
						if(element.isSelected())
						{
							ContentValues cv = new ContentValues();
							cv.put("songTitle", songsList.get(j).get("songTitle"));
							cv.put("songPath",songsList.get(j).get("songPath"));
							db.insert("playlist", null, cv);
							cv.clear();
						}
					}
					db.close();	
					Toast.makeText(getBaseContext(),"New songs have been added to the songlist.", Toast.LENGTH_LONG).show();
					finish();
				}
			});
		}
	private List<Model> getModel() {
		// TODO Auto-generated method stub
		List<Model> list = new ArrayList<Model>();
		for(int i=0;i<songsList.size();i++)
			list.add(new Model(songsList.get(i).get("songTitle").trim()));
		return list;
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
