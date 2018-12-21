package com.shivsau.motiondetect;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;

public class SensorReceive extends BroadcastReceiver implements SensorEventListener {

	Context con;
	SensorManager sm=null;
	Sensor s;
	WakeLock wl=null;
	Vibrator v;
	float y[]=new float[20];
	float x[]=new float[20];
	float z[]=new float[20];
	int j=0;
	int sensorDelay=0;
	
	@SuppressLint("Wakelock")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		con=context;
		if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
			sensorDelay = sp.getInt("sensorVal", 0);
			PowerManager pm= (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			wl= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "yoyo");
			wl.acquire();
			v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
			s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			for(int i=0;i<20;i++)
			{
				x[i]=0;
				y[i]=0;
				z[i]=0;
			}
			reg();
			unreg();
			reg();
			
		}
		if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
		{
			if(wl!=null)
			wl.release();
			if(sm!=null)
			unreg();
		}
	}

	private void unreg() {
		// TODO Auto-generated method stub
		if(sm!=null)
		sm.unregisterListener(this);
	}

	private void reg() {
		// TODO Auto-generated method stub
		sm.registerListener(this, s, sensorDelay);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
	
		float xacc=event.values[0];
		float yacc=event.values[1];
		float zacc=event.values[2];
		if(j<20)
		{
			x[j]=xacc;
			y[j]=yacc;
			z[j]=zacc;
			j++;
		}else if(j==20)
		{
			for(int i=0;i<19;i++)
			{
				x[i]=x[i+1];
				y[i]=y[i+1];
				z[i]=z[i+1];
			}
			x[19]=xacc;
			y[19]=yacc;
			z[19]=zacc;
		}
		if(y[19]<-7.0&&y[19]>-11.0)
		{
			
			int county=0;
			int countx=0;
			int countz=0;
			int countxm=0;
			int negx=0;
			int negy=0;
			int negxx=0;
			if(y[0]>6.5&&y[0]<11.5)
			{
				for(int k=0;k<19;k++)
				{
					if(y[k]>y[k+1])
						county++;
					else if(y[k]==y[k+1]&&k!=0&&y[k-1]!=y[k])
						county++;
					if(y[k]>12.0||y[k]<-12.0)
						negy++;
					if(x[k]>x[k+1])
						countx++;
					else if(x[k]<x[k+1])
						countxm++;
					if(x[k]>-1.5&&x[k]<1.0)
						negxx++;
					if(x[k]<-11.0||x[k]>11.0)
						negx++;
					if(k==18&&(x[k+1]<-11.0||x[k+1]>11.0))
							negx++;
					if(z[k]>-4.0&&z[k]<4.0)
						countz++;
				}
				if(((county>=12&&countx>=7&&countz>=15)||(county>=10&&countxm>=7&&countz>=15))&&negx<=2&&(Math.abs(x[0]-x[19])<15.0)&&negy<=3&&negxx<11)
				{
					sm.unregisterListener(this);
					v.vibrate(500);
					
					Intent i= new Intent(con, AboveLock.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					con.startActivity(i);
				}
			}
		}
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	
}
