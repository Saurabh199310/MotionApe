package com.shivsau.motiondetect;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class SongsManager {
	Context con;
	public SongsManager(Context context){
		con=context;
	}
	
	
	public ArrayList<HashMap<String, String>> getPlayList(){
		ArrayList<HashMap<String, String>> songsList1 = new ArrayList<HashMap<String, String>>();
		Cursor mCursor = con.getContentResolver().query(
	            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	            new String[] { MediaStore.Audio.Media.DISPLAY_NAME,
	                    MediaStore.Audio.Media.DATA }, null, null, null);
	    HashMap<String, String> songMap;
	    while (mCursor.moveToNext()) {
	        songMap = new HashMap<String, String>();
	       
	        songMap.put("songTitle",mCursor.getString(mCursor
	                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
	        songMap.put("songPath", mCursor.getString(mCursor
	                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
	        if(songMap.get("songTitle").endsWith(".mp3")||songMap.get("songTitle").endsWith(".MP3"))
	        songsList1.add(songMap);
	    }
	    mCursor.close();
	    Cursor mCursor2 = con.getContentResolver().query(
	            MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
	            new String[] { MediaStore.Audio.Media.DISPLAY_NAME,
	                    MediaStore.Audio.Media.DATA }, null, null, null);
	    
	    while (mCursor2.moveToNext()) {
	        songMap = new HashMap<String, String>();
	       
	        songMap.put("songTitle",mCursor2.getString(mCursor2
	                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
	        songMap.put("songPath", mCursor2.getString(mCursor2
	                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
	        if(songMap.get("songTitle").endsWith(".mp3")||songMap.get("songTitle").endsWith(".MP3"))
	        songsList1.add(songMap);
	    }
	    mCursor2.close();
	    return songsList1;
	}
}
