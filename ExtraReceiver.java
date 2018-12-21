package com.shivsau.motiondetect;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ExtraReceiver extends BroadcastReceiver {
	
	private static PendingIntent pi3;
	private static PendingIntent pi4;


	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("com.shivsau.motiondetect.ALARMCLOSETWO"))
		{
			Intent i2 = new Intent(context, AllTimeService.class);
			context.stopService(i2);
			Intent i3 = new Intent();
			i3.setAction("com.shivsau.motiondetect.CLOSENOTIF");
			context.sendBroadcast(i2);
			Intent i4= new Intent();
			i4.setAction("com.shivsau.motiondetect.WANTFLASHOFF");
			context.sendBroadcast(i3);
		}
		if(intent.getAction().equals("com.shivsau.motiondetect.STARTFORREPEAT"))
		{
			Intent i4 = new Intent(context, AllTimeService.class);
			context.startService(i4);
			Intent rep = new Intent();
			rep.setAction("com.shivsau.motiondetect.REPEATYES");
			context.sendBroadcast(rep);
		}
		if(intent.getAction().equals("com.shivsau.motiondetect.REPEATYES"))
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
			int starthr = Integer.parseInt(sp.getString("fhr", "00"));
			int startmin = Integer.parseInt(sp.getString("fmin", "00"));
			int endhr = Integer.parseInt(sp.getString("thr", "00"));
			int endmin = Integer.parseInt(sp.getString("tmin", "00"));
			
			
			int flg=0;
			Intent i= new Intent();
			i.setAction("com.shivsau.motiondetect.ALARMCLOSETWO");
			pi3= PendingIntent.getBroadcast(context, 121, i, 0);
			AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Calendar time = Calendar.getInstance();
			Calendar time1 = Calendar.getInstance();
			time1.set(Calendar.HOUR_OF_DAY, starthr);
			time1.set(Calendar.MINUTE, startmin);
			long val2=time1.getTimeInMillis();
			if(time.get(Calendar.HOUR_OF_DAY)>starthr)
			{
				flg=1;
				val2 = val2 + 86400000;
			}else if(time.get(Calendar.HOUR_OF_DAY)==starthr && time.get(Calendar.MINUTE)>=startmin)
			{
				flg=1;
				val2=val2+86400000;
			}
			
		    alarm.setExact(AlarmManager.RTC_WAKEUP, val2, pi3);
		    
			Calendar time2 = Calendar.getInstance();
			time2.set(Calendar.HOUR_OF_DAY, endhr);
			time2.set(Calendar.MINUTE, endmin);
			long val=time2.getTimeInMillis();
			if(endhr<starthr)
			{
				val+= 86400000;
			}else if(endhr==starthr && endmin<=startmin)
			{
				val+= 86400000;
			}
			if(flg==1)
				val=val+86400000;
			Intent i2= new Intent(context, ExtraReceiver.class);
			i2.setAction("com.shivsau.motiondetect.STARTFORREPEAT");
			pi4= PendingIntent.getBroadcast(context, 51, i2, 0);
			AlarmManager alarm2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			alarm2.setExact(AlarmManager.RTC_WAKEUP, val, pi4);
		    
		}
		if(intent.getAction().equals("com.shivsau.motiondetect.CANCELREPEAT"))
		{
			if(pi3!=null||pi4!=null)
			{
				AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pi3);
				alarm.cancel(pi4);
			}
		}
	}

}
