package com.shivsau.motiondetect;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class AboveLock extends Activity {
	
	public static Camera cam = null;
	public static boolean flash=false;
	public static boolean play=false;
	ImageView bt;
	ImageView wf;
	ImageView fl;
	ImageView pl;
	int clickCount=0;
	Handler handle;
	Runnable r;
	int flg=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lock_lay);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9900")));
		bt= (ImageView) findViewById(R.id.bluet);
		wf= (ImageView) findViewById(R.id.wiFi);
		fl= (ImageView) findViewById(R.id.fl);
		pl= (ImageView) findViewById(R.id.music_Btn);
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    if (mBluetoothAdapter.isEnabled())
	        bt.setImageResource(R.drawable.blue_btn_on);
	    
	    WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE); 
		if(wifi.isWifiEnabled())
			wf.setImageResource(R.drawable.wifi_on);
		
		if(!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
			fl.setEnabled(false);
		if(flash==true)
			fl.setImageResource(R.drawable.flash_on);
		if(play)
			pl.setImageResource(R.drawable.stop_btn);
			
		handle=new Handler();
		r=new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(clickCount==0)
					performDefault();
					finish();
			}
			
		};
		handle.postDelayed(r, 5000);
	}


	public void blueTooth(View v)
	{
		clickCount++;
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    if (!mBluetoothAdapter.isEnabled())
	    {
	    	mBluetoothAdapter.enable();
	    	bt.setImageResource(R.drawable.blue_btn_on);
	    }
	    if (mBluetoothAdapter.isEnabled())
	    {
	    	mBluetoothAdapter.disable();
	    	bt.setImageResource(R.drawable.blue_btn_off);
	    }
	}
	
	public void wiFi(View v)
	{
		clickCount++;
		WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE); 

		if(!wifi.isWifiEnabled())
		{
			wifi.setWifiEnabled(true);
			wf.setImageResource(R.drawable.wifi_on);
		}
		else
		{
			wifi.setWifiEnabled(false);
			wf.setImageResource(R.drawable.wifi_off);
		}
	}
	
	@SuppressLint("NewApi")
	public void flashHandle(View v)
	{
		clickCount++;
		if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)&&!flash){
			flash=true;
			fl.setImageResource(R.drawable.flash_on);
			cam = Camera.open();
            Parameters p = cam.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            
            NotificationManager notif = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.flash_notify);
            Intent i = new Intent("com.shivsau.motiondetect.WANTFLASHOFF");
            PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 11, i, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.flash_off, pending);
            if(Build.VERSION.SDK_INT>=16)
            {
            Notification.Builder builder = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher).setContentTitle("App_Name").
                		setContent(rv);
            builder.setAutoCancel(true);
            notif.notify(33, builder.build());
            }else
            {
            	@SuppressWarnings("deprecation")
				Notification not = new Notification(R.drawable.ic_launcher, null, System.currentTimeMillis());
            	not.contentView=rv;
            	not.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
                not.defaults |= Notification.DEFAULT_LIGHTS;
                notif.notify(33,not);
            }
		}
		else{
			if (getApplicationContext().getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_FLASH)&& flash) {
			            cam.stopPreview();
			            cam.release();
			            cam = null;
			            flash=false;
			            fl.setImageResource(R.drawable.flash_off);
			            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			            nm.cancel(33);
		}}
	}
	
	public void callNo(View v)
	{
		clickCount++;
		MyHelper mh =new MyHelper(this);
		SQLiteDatabase db = mh.getReadableDatabase();
		Cursor c= db.rawQuery("SELECT phoneno FROM prefs", null);
		while(c.moveToNext())
		{
			if(!c.getString(0).trim().equals("a"))
			{
			Intent i= new Intent(android.content.Intent.ACTION_CALL);
			if(Build.VERSION.SDK_INT<=20)
			i.setPackage("com.android.phone");
			i.setData(Uri.parse("tel:"+c.getString(0).trim()));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
		}
		c.close();
		db.close();
		
	}
	
	public void music(View v)
	{
		clickCount++;
		MyHelper2 mh2 = new MyHelper2(this);
		SQLiteDatabase db2 = mh2.getReadableDatabase();
		Cursor cur = db2.rawQuery("SELECT * FROM playlist", null);
		if((!play) && cur.getCount()>0)
		{
			play=true;
			pl.setImageResource(R.drawable.stop_btn);
			Intent i1= new Intent();
			i1.setAction("com.shivsau.motiondetect.PLAYMUSIC");
			sendBroadcast(i1);
		}else
		{
			play=false;
			pl.setImageResource(R.drawable.play_btn_main);
			Intent i2= new Intent();
			i2.setAction("com.shivsau.motiondetect.STOPMUSIC");
			sendBroadcast(i2);
		}
		cur.close();
		db2.close();
	}

	@SuppressLint("NewApi")
	public void performDefault() {
		// TODO Auto-generated method stub
		View v = new View(this);
		MyHelper mh =new MyHelper(this);
		SQLiteDatabase db = mh.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM prefs", null);
		while(c.moveToNext())
		{
			if(c.getInt(0)==1)
				blueTooth(v);
			if(c.getInt(1)==1)
				wiFi(v);
			if(c.getInt(2)==1)
				flashHandle(v);
			if(c.getInt(3)==1)
				music(v);
			if(c.getInt(4)==1)
				callNo(v);
		}
		c.close();
		db.close();	
	}
	public void editNote(View v)
	{
		clickCount++;
		Intent note= new Intent(this, NotepadLock.class);
		note.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		note.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		note.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(note);
	}
}
