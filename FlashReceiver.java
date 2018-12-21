package com.shivsau.motiondetect;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FlashReceiver extends BroadcastReceiver {
	

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("com.shivsau.motiondetect.WANTFLASHOFF"))
		{
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(33);
			if(AboveLock.cam!=null)
			{
            AboveLock.cam.release();
            AboveLock.cam = null;
            AboveLock.flash=false;
			}
		}
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
		Intent i = new Intent(context, AllTimeService.class);
		context.startService(i);
		}
		
		if(intent.getAction().equals("com.shivsau.motiondetect.ALARMCLOSE"))
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
	}
}
