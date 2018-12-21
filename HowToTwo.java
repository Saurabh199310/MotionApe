package com.shivsau.motiondetect;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.VideoView;

public class HowToTwo extends Fragment implements OnTouchListener {
	
	VideoView motionVid;
	Uri uri;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.how_to_page_two, container, false);
		motionVid = (VideoView) v.findViewById(R.id.vid);
		uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.motion);
		motionVid.setVideoURI(uri);
		motionVid.setOnTouchListener(this);
		return v;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(motionVid.isPlaying())
		{
			motionVid.pause();
			motionVid.setVideoURI(uri);
		}
		motionVid.start();
		return false;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(this.isVisible())
		{
			if(isVisibleToUser)
				motionVid.setVisibility(View.VISIBLE);
			else
				motionVid.setVisibility(View.GONE);
		}
	}
}
