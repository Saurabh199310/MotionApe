package com.shivsau.motiondetect;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;

public class AppSettings extends Fragment implements OnClickListener {
	
	Button b;
	Spinner fhr;
	Spinner fmin;
	Spinner thr;
	Spinner tmin;
	CheckBox setAlarm;
	TableLayout l1;
	Button b2;
	CheckBox rep;
	int starthr;
	int startmin;
	int endhr;
	int endmin;
	int cnt=0;
	int cnt2=0;
	boolean toRepeat=false;
	String s1="00";
	String s2="00";
	String s3="00";
	String s4="00";
	ArrayAdapter<String> a1;
	ArrayAdapter<String> a2;
	ArrayAdapter<String> a3;
	ArrayAdapter<String> a4;
	private static PendingIntent pi;
	private static PendingIntent pi2;
	int flag=0;
	String hr[]= {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
	String min[]={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
			"24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46",
			"47","48","49","50","51","52","53","54","55","56","57","58","59"};
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View bigv = inflater.inflate(R.layout.settings_layout,container, false);
		b=(Button) bigv.findViewById(R.id.startStop);
		b2= (Button) bigv.findViewById(R.id.setTime);
		rep=(CheckBox) bigv.findViewById(R.id.repeat);
		l1 =(TableLayout) bigv.findViewById(R.id.ll1);
		l1.setVisibility(View.GONE);
		
		setAlarm = (CheckBox) bigv.findViewById(R.id.allowMode);
		fhr=(Spinner) bigv.findViewById(R.id.fhr);
		fmin=(Spinner) bigv.findViewById(R.id.fmin);
		thr=(Spinner) bigv.findViewById(R.id.thr);
		tmin=(Spinner) bigv.findViewById(R.id.tmin);
		a1 =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, hr);
		a2 =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, min);
		a3 =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, hr);
		a4 =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, min);
		fhr.setAdapter(a1);
		fmin.setAdapter(a2);
		thr.setAdapter(a3);
		tmin.setAdapter(a4);
		ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (service.service.getClassName().equals("com.shivsau.motiondetect.AllTimeService")) {
	        	flag=1;
	        }
	    }
	    if(flag==0)
	    	b.setText("Start Service");
		b.setOnClickListener(this);
		b2.setOnClickListener(this);
		
		fhr.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				starthr=Integer.parseInt(parent.getSelectedItem().toString());
				s1=parent.getSelectedItem().toString();
				cnt2=cnt+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		fmin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startmin=Integer.parseInt(parent.getSelectedItem().toString());
				s2=parent.getSelectedItem().toString();
				cnt2=cnt+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		thr.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				endhr=Integer.parseInt(parent.getSelectedItem().toString());
				s3=parent.getSelectedItem().toString();
				cnt2=cnt+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		tmin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				endmin=Integer.parseInt(parent.getSelectedItem().toString());
				s4=parent.getSelectedItem().toString();
				cnt2=cnt+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setAlarm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					
					l1.setVisibility(View.VISIBLE);
					fhr.setSelection(0);
					fmin.setSelection(0);
					thr.setSelection(0);
					tmin.setSelection(0);
					rep.setChecked(false);
				}else
				{
					cnt=0;
					cnt2=0;
					if(pi!=null||pi2!=null)
					{
						AlarmManager al = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
						al.cancel(pi);
						al.cancel(pi2);
						pi=null;
						pi2=null;
					}
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
					if(sp.getBoolean("rep", false))
					{
						Intent cncl = new Intent();
						cncl.setAction("com.shivsau.motiondetect.CANCELREPEAT");
						getActivity().sendBroadcast(cncl);
					}
					
					l1.setVisibility(View.GONE);
				}
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				sp.edit().putBoolean("AllowModeVal", isChecked).commit();
			}
		});
		
		rep.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				toRepeat=isChecked;
				cnt2=cnt+1;
			}
		});
		return bigv;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.startStop)
		{
		if(flag==0)
		{
			flag=1;
			b.setText("Stop Service");
			Intent i = new Intent(getActivity(), AllTimeService.class);
			getActivity().startService(i);
		}else
		{
			flag=0;
			b.setText("Start Service");
			Intent i = new Intent(getActivity(), AllTimeService.class);
			getActivity().stopService(i);
			Intent i2 = new Intent();
			i2.setAction("com.shivsau.motiondetect.CLOSENOTIF");
			getActivity().sendBroadcast(i2);
			Intent i3= new Intent();
			i3.setAction("com.shivsau.motiondetect.WANTFLASHOFF");
			getActivity().sendBroadcast(i3);
		}
		}
		if(v.getId()==R.id.setTime)
		{
			startAlarm();
		Toast.makeText(getActivity(),"Time interval has been set.", Toast.LENGTH_LONG).show();
	}
	}
	
	@SuppressLint("NewApi")
	public void startAlarm()
	{
		cnt=cnt+1;
		int flg=0;
		Intent i= new Intent(getActivity(), FlashReceiver.class);
		i.setAction("com.shivsau.motiondetect.ALARMCLOSE");
		pi= PendingIntent.getBroadcast(getActivity(), 121, i, 0);
		AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
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
		if(!toRepeat)
		{
		if (android.os.Build.VERSION.SDK_INT >= 19) {
	        alarm.setExact(AlarmManager.RTC_WAKEUP, val2, pi);
	    } else {
	        alarm.set(AlarmManager.RTC_WAKEUP, val2, pi);
	    }
		}
		else
		{
			if (android.os.Build.VERSION.SDK_INT <= 19)
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, val2,AlarmManager.INTERVAL_DAY, pi);
				
		}
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
		Intent i2= new Intent(getActivity(), AllTimeService.class);
		pi2= PendingIntent.getService(getActivity(), 51, i2, 0);
		AlarmManager alarm2 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		if(!toRepeat)
		{
		if (android.os.Build.VERSION.SDK_INT >= 19) {
	        alarm2.setExact(AlarmManager.RTC_WAKEUP, val, pi2);
	    } else {
	        alarm2.set(AlarmManager.RTC_WAKEUP, val, pi2);
	    }
		}else
		{
			onPause();
			if (android.os.Build.VERSION.SDK_INT >= 19) {
		        Intent formReceiver = new Intent();
		        formReceiver.setAction("com.shivsau.motiondetect.REPEATYES");
		        getActivity().sendBroadcast(formReceiver);
		    } else {
		        alarm2.setRepeating(AlarmManager.RTC_WAKEUP, val,AlarmManager.INTERVAL_DAY, pi2);
		    }	
		}
		SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		p1.edit().putBoolean("AllowModeVal", setAlarm.isChecked()).commit();
		p1.edit().putString("fhr", s1).commit();
		p1.edit().putString("fmin", s2).commit();
		p1.edit().putString("thr", s3).commit();
		p1.edit().putString("tmin", s4).commit();
		p1.edit().putBoolean("rep", rep.isChecked()).commit();
		p1.edit().putInt("cnt", cnt).commit();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if((cnt==cnt2 && cnt==0)||(cnt2>cnt && cnt==0))
		{
			p1.edit().putBoolean("AllowModeVal", false).commit();
			p1.edit().putString("fhr", "00").commit();
			p1.edit().putString("fmin", "00").commit();
			p1.edit().putString("thr", "00").commit();
			p1.edit().putString("tmin", "00").commit();
			p1.edit().putBoolean("rep", false).commit();
			p1.edit().putInt("cnt", cnt).commit();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
		setAlarm.setChecked(p1.getBoolean("AllowModeVal", false));
		fhr.setSelection(a1.getPosition(p1.getString("fhr", "00")));
		fmin.setSelection(a2.getPosition(p1.getString("fmin", "00")));
		thr.setSelection(a3.getPosition(p1.getString("thr", "00")));
		tmin.setSelection(a4.getPosition(p1.getString("tmin", "00")));
		rep.setChecked(p1.getBoolean("rep", false));
		cnt=p1.getInt("cnt", 0);
		cnt2=cnt;
		if(setAlarm.isChecked())
		{
			l1.setVisibility(View.VISIBLE);
		}
		toRepeat=rep.isChecked();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(this.isVisible())
		{
			if(isVisibleToUser)
				onResume();
			else
				onPause();
		}
	}

}
