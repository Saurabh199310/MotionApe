package com.shivsau.motiondetect;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

	public MyHelper(Context context) {
		super(context, "SavePref", null, 1);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try{
			db.execSQL("create table prefs(bt INTEGER NOT NULL, wifi INTEGER NOT NULL, flash INTEGER NOT NULL," +
					" song INTEGER NOT NULL, call INTEGER NOT NULL, phoneno TEXT, name TEXT);");
					
			}catch(SQLException e){
				e.printStackTrace();
			}
	}
	
	public void delete(SQLiteDatabase db){
		db.execSQL("delete * from prefs");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
