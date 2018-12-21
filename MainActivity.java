package com.shivsau.motiondetect;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Fragment implements android.view.View.OnClickListener{

	MyHelper mh;
	Switch b;
	Switch w;
	Switch f;
	Switch s;
	Switch ca;
	TextView tv;
	ImageView ref;
	TextView appName;
	TableRow tr1;
	TableRow tr2;
	AutoCompleteTextView et;
	Spinner spin;
	ArrayList<String> contacts;
	ArrayList<String> contactno;
	String phone_no="a";
	String name_select="yo";
	public static Camera cam = null;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.activity_main2, container, false);
	
		ref=(ImageView) v.findViewById(R.id.toref);
		tr1=(TableRow)v.findViewById(R.id.todelrow);
		tr2=(TableRow)v.findViewById(R.id.toDel);
		b=(Switch) v.findViewById(R.id.bluettoth);
		w=(Switch) v.findViewById(R.id.wifi);
		f=(Switch) v.findViewById(R.id.flash);
		s=(Switch) v.findViewById(R.id.song);
		ca=(Switch) v.findViewById(R.id.call);
		et=(AutoCompleteTextView) v.findViewById(R.id.contact);
		spin=(Spinner) v.findViewById(R.id.number);
		tv=(TextView) v.findViewById(R.id.textView5);
		spin.setVisibility(View.GONE);
		ref.setOnClickListener(this);
		
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mh=new MyHelper(getActivity());
		SQLiteDatabase db=mh.getReadableDatabase();
		Cursor c=db.rawQuery("SELECT * FROM prefs",null);
		while(c.moveToNext()){
			if(c.getInt(0)==1)
				b.setChecked(true);
			if(c.getInt(1)==1)
				w.setChecked(true);
			if(c.getInt(2)==1)
				f.setChecked(true);
			if(c.getInt(3)==1)
				s.setChecked(true);
			if(c.getInt(4)==1)
				ca.setChecked(true);
			name_select=c.getString(6);
			phone_no=c.getString(5);
			
			ReadContacts();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, contacts);
			et.setAdapter(adapter);
			
			if(!(c.getString(5).equals("a")))
			{
				et.setText(c.getString(6));
				String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" +c.getString(6) +"%'";
				String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
				Cursor cu = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				        projection, selection, null, null);
				
				contactno=new ArrayList<String>();
				
				while(cu.moveToNext())
			    {
			    contactno.add(cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			    }
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, contactno);
				spin.setAdapter(adapter2);
				spin.setVisibility(View.VISIBLE);
				ref.setVisibility(View.VISIBLE);
				spin.setSelection(adapter2.getPosition(c.getString(5)));
			}
		}
		c.close();
		db.close();

		if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
		{
			tr1.setVisibility(View.GONE);
			tr2.setVisibility(View.GONE);
		}
		et.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				name_select=(String) parent.getItemAtPosition(position);
				String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name_select +"%'";
				String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
				Cursor c = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				        projection, selection, null, null);
				
				contactno=new ArrayList<String>();
				
				while(c.moveToNext())
			    {
			    contactno.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			    }
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, contactno);
				spin.setAdapter(adapter2);
				spin.setVisibility(View.VISIBLE);
				ref.setVisibility(View.VISIBLE);
				spin.setOnItemSelectedListener(new OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> parent2,
							View view2, int position2, long id2) {
						// TODO Auto-generated method stub
						phone_no=(String)parent2.getItemAtPosition(position2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		int val;
		mh = new MyHelper(getActivity());
		SQLiteDatabase db2=mh.getWritableDatabase();
		db2.execSQL("delete from prefs");
		ContentValues cv = new ContentValues();
		if(b.isChecked())
			val=1;
		else
			val=0;
		cv.put("bt", val);
		if(w.isChecked())
			val=1;
		else
			val=0;
		cv.put("wifi", val);
		if(f.isChecked())
			val=1;
		else
			val=0;
		cv.put("flash", val);
		if(s.isChecked())
			val=1;
		else
			val=0;
		cv.put("song", val);
		if(ca.isChecked())
			val=1;
		else
			val=0;
		cv.put("call", val);
		cv.put("phoneno", phone_no);
		cv.put("name", name_select);
		db2.insert("prefs", null, cv);
		db2.close();
		cv.clear();
	}

	private void ReadContacts() {
		// TODO Auto-generated method stub
		contacts= new ArrayList<String>();
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
	    String[] projection = new String[] { ContactsContract.Contacts._ID,
	            ContactsContract.Contacts.DISPLAY_NAME };
	    String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER
	            + " = '1'";
	    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
	            + " COLLATE LOCALIZED ASC";

	    Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, projection,
	            selection, null, sortOrder);
	    while(cursor.moveToNext())
	    {
	    contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
	}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		name_select="yo";
		phone_no="a";
		spin.setVisibility(View.GONE);
		ReadContacts();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, contacts);
		et.setAdapter(adapter);
		et.setText("");
		ref.setVisibility(View.GONE);
	}
	
}
