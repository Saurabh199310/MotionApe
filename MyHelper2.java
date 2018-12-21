package com.shivsau.motiondetect;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper2 extends SQLiteOpenHelper{

	Context con;
	public MyHelper2(Context context) {
		super(context,"MyData", null,1);	
		con=context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
		db.execSQL("create table playlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,songTitle TEXT NOT NULL,songPath TEXT NOT NULL);");		
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}

