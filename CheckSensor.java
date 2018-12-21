package com.shivsau.motiondetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;

public class CheckSensor extends Fragment implements OnCheckedChangeListener,android.widget.CompoundButton.OnCheckedChangeListener,
OnClickListener, SensorEventListener{

	SensorManager sm=null;
	Sensor s;
	TableLayout toDis;
	TableLayout toHide;
	CheckBox setSensor;
	CheckBox viewSteps;
	RadioGroup freq;
	RadioButton fast;
	RadioButton game;
	RadioButton userInterface;
	RadioButton normal;
	Button set;
	TextView msg;
	int sensorConstant=0;
	Vibrator v;
	float y[]=new float[20];
	float x[]=new float[20];
	float z[]=new float[20];
	int j=0;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.check_sensor_layout, container,false);
		toHide = (TableLayout) view.findViewById(R.id.viewTable);
		toDis = (TableLayout) view.findViewById(R.id.toDisable);
		setSensor = (CheckBox) view.findViewById(R.id.configure);
		viewSteps = (CheckBox) view.findViewById(R.id.viewSteps);
		freq = (RadioGroup) view.findViewById(R.id.radiogrp);
		fast = (RadioButton) view.findViewById(R.id.fast);
		game = (RadioButton) view.findViewById(R.id.game);
		userInterface = (RadioButton) view.findViewById(R.id.useri);
		normal = (RadioButton) view.findViewById(R.id.normal);
		set = (Button) view.findViewById(R.id.setDelay);
		msg = (TextView) view.findViewById(R.id.detect);
		sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
		s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		v = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		return view;
	}

	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if(checkedId==R.id.fast)
		{
			sensorConstant=0;
			unreg();
			reg(sensorConstant);
		}
		if(checkedId==R.id.game)
		{
			sensorConstant=1;
			unreg();
			reg(sensorConstant);
		}
		if(checkedId==R.id.useri)
		{
			sensorConstant=2;
			unreg();
			reg(sensorConstant);
		}
		if(checkedId==R.id.normal)
		{
			sensorConstant=3;
			unreg();
			reg(sensorConstant);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(buttonView.getId()==R.id.configure)
		{
			if(isChecked)
			{
				viewSteps.setEnabled(true);
				set.setEnabled(true);
				fast.setEnabled(true);
				game.setEnabled(true);
				userInterface.setEnabled(true);
				normal.setEnabled(true);
				reg(sensorConstant);
			}
			else
			{
				toHide.setVisibility(View.GONE);
				viewSteps.setChecked(false);
				viewSteps.setEnabled(false);
				set.setEnabled(false);
				fast.setEnabled(false);
				game.setEnabled(false);
				userInterface.setEnabled(false);
				normal.setEnabled(false);
				unreg();
			}
		}
		if(buttonView.getId()==R.id.viewSteps)
		{
			if(isChecked)
				toHide.setVisibility(View.VISIBLE);
			else
				toHide.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
		p.edit().putInt("sensorVal", sensorConstant).commit();
		Toast.makeText(getActivity(),"Selected frequency has been set.", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
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
					msg.setText("Motion Detected");
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							msg.setText("");
							reg(sensorConstant);
						}
					}, 1000);
				}
			}
		}
	}
	
	public void reg(int delayConstant)
	{
		sm.registerListener(this, s, delayConstant);
	}
	
	public void unreg()
	{
		if(sm!=null)
			sm.unregisterListener(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		setSensor.setChecked(false);
		viewSteps.setChecked(false);
		viewSteps.setEnabled(false);
		set.setEnabled(false);
		fast.setEnabled(false);
		game.setEnabled(false);
		userInterface.setEnabled(false);
		normal.setEnabled(false);
		toHide.setVisibility(View.GONE);
		
		unreg();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences s=PreferenceManager.getDefaultSharedPreferences(getActivity());
		sensorConstant = s.getInt("sensorVal", 0);
		
		freq.setOnCheckedChangeListener(this);
		setSensor.setOnCheckedChangeListener(this);
		viewSteps.setOnCheckedChangeListener(this);
		set.setOnClickListener(this);
		
		toHide.setVisibility(View.GONE);
		
		viewSteps.setEnabled(false);
		set.setEnabled(false);
		fast.setEnabled(false);
		game.setEnabled(false);
		userInterface.setEnabled(false);
		normal.setEnabled(false);
		
		freq.check(freq.getChildAt(sensorConstant).getId());
		
		reg(sensorConstant);
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
