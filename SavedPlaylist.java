package com.shivsau.motiondetect;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SavedPlaylist extends Fragment implements OnClickListener {

	ListView lv;
	Button newpl;
	
@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	View v=inflater.inflate(R.layout.database_playlist,container, false);
	newpl=(Button) v.findViewById(R.id.newPlay);
	newpl.setOnClickListener(this);
	lv=(ListView)v.findViewById(R.id.list2);
		return v;
	}

@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	String [] from={"songTitle"};
	int [] to={R.id.finalText};
	MyHelper2 mh=new MyHelper2(getActivity());
	SQLiteDatabase db=mh.getReadableDatabase();
	Cursor cr=db.rawQuery("SELECT _id,songTitle FROM playlist", null);
	SimpleCursorAdapter adapt=new SimpleCursorAdapter(getActivity(), R.layout.database_playlist_item, cr, from, to,0);
	lv.setAdapter(adapt);
	db.close();
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	Intent i= new Intent(getActivity(),PlayListActivity.class);
	startActivity(i);
}

}
