package com.shivsau.motiondetect;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

@SuppressLint("NewApi")
public class AllTimeService extends Service {

	BroadcastReceiver mReceiver=null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new SensorReceive();
        registerReceiver(mReceiver, filter);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(this, SettingsMain.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this,0,myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
		if(Build.VERSION.SDK_INT>=16)
		{
		Notification notify= new Notification.Builder(this).
				setSmallIcon(R.drawable.ic_launcher).
				setContentTitle("MotionApe Service is running.").
				setContentIntent(pendingIntent).
				setWhen(System.currentTimeMillis()).build();
		
		startForeground(21, notify);
		}else
		{
		Notification notif = new Notification(R.drawable.ic_launcher, null, System.currentTimeMillis());
		notif.setLatestEventInfo(this, "MotionApe Service is running.", null, pendingIntent);
		startForeground(21, notif);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
        if(mReceiver!=null)
         unregisterReceiver(mReceiver);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
