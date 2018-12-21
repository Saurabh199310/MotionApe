package com.shivsau.motiondetect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class MusicManager extends BroadcastReceiver implements OnCompletionListener {
	
	private static MediaPlayer mp;
	Context con;
	MyHelper2 mh;
	static ArrayList<HashMap<String,String>> songs= new ArrayList<HashMap<String,String>>();
	static int currentSongIndex=0;
	private static final int NOTIF_ID = 786;
	private static NotificationCompat.Builder mBuilder;
	private static NotificationManager mNotificationManager;
	private static RemoteViews mRemoteViews;
	private static Notification mNotification;
	private static boolean isShuffle = false;
	private static boolean isRepeat = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		con=context;
		if(intent.getAction().equals("com.shivsau.motiondetect.PLAYMUSIC"))
		{
			songs= new ArrayList<HashMap<String,String>>();
			currentSongIndex=0;
			mh =new MyHelper2(context);
			SQLiteDatabase db = mh.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT * FROM playlist", null);
			HashMap<String, String> songMap;
			while(c.moveToNext())
			{
				songMap = new HashMap<String, String>();
				songMap.put("songTitle",c.getString(1));
				songMap.put("songPath",c.getString(2));
				songs.add(songMap);
			}
			c.close();
			db.close();
			
			mp= new MediaPlayer();
			mp.setOnCompletionListener(this);
			if(songs.size()>0)
			{
			setNotification();
			playSong(0);
			}
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.STOPMUSIC"))
		{
			if(mp!=null)
			{
			mp.release();
			mp=null;
			}
			isShuffle = false;
			isRepeat = false;
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(786);
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.CLOSENOTIF"))
		{
			if(mp!=null)
			{
			mp.release();
			mp=null;
			}
			isShuffle = false;
			isRepeat = false;
			AboveLock.play=false;
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(786);
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.PAUSERESUME"))
		{
			if(mp.isPlaying()){
				if(mp!=null){
					mp.pause();
					mRemoteViews.setImageViewResource(R.id.playPause, R.drawable.play_btn);
				}
			}else{
				if(mp!=null){
					mp.start();
					mRemoteViews.setImageViewResource(R.id.playPause, R.drawable.pause_btn);
				}
			}
			updateNotification();
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.NEXTSONG"))
		{
			if(currentSongIndex < (songs.size() - 1)){
				currentSongIndex = currentSongIndex + 1;
				playSong(currentSongIndex);			
			}else
			{
				currentSongIndex = 0;
				playSong(0);
			}
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.PREVIOUSSONG"))
		{
			if(currentSongIndex > 0){
				currentSongIndex = currentSongIndex - 1;
				playSong(currentSongIndex);
			}else{
				currentSongIndex = songs.size() - 1;
				playSong(songs.size() - 1);
			}
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.SHUFFLESONG"))
		{
			if(isShuffle)
			{
				isShuffle = false;
				mRemoteViews.setImageViewResource(R.id.shuffle, R.drawable.shuffle_r_btn_off);
			}else
			{
				isShuffle = true;
				isRepeat = false;
				mRemoteViews.setImageViewResource(R.id.repeat, R.drawable.shuffle_btn_off);
				mRemoteViews.setImageViewResource(R.id.shuffle, R.drawable.shuffle_r_btn_on);
			}
			updateNotification();
		}
		else if(intent.getAction().equals("com.shivsau.motiondetect.REPEATSONG"))
		{
			if(isRepeat)
			{
				isRepeat = false;
				mRemoteViews.setImageViewResource(R.id.repeat, R.drawable.shuffle_btn_off);
			}
			else
			{
				isRepeat = true;
				isShuffle = false;
				mRemoteViews.setImageViewResource(R.id.shuffle, R.drawable.shuffle_r_btn_off);
				mRemoteViews.setImageViewResource(R.id.repeat, R.drawable.shuffle_btn_on);
			}
			updateNotification();
		}
	}

	private void playSong(int i) {
		// TODO Auto-generated method stub
		try {
        	mp.reset();
			mp.setDataSource(songs.get(i).get("songPath"));
			mp.prepare();
			mp.start();
			mRemoteViews.setImageViewResource(R.id.playPause, R.drawable.pause_btn);
			updateNotification();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if(isRepeat){
			playSong(currentSongIndex);
		} else if(isShuffle){
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songs.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else
		{
		if(currentSongIndex < (songs.size() - 1)){
			currentSongIndex = currentSongIndex + 1;
			playSong(currentSongIndex);
		}else{
			currentSongIndex = 0;
			playSong(0);
		}
		}
		updateNotification();
	}

	@SuppressWarnings("deprecation")
	private void setNotification() {
		// TODO Auto-generated method stub
		mNotificationManager = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent myIntent = new Intent(con, MainScreen.class);
		myIntent.putExtra("fromNotif", 'y');
	    PendingIntent pendingIntent = PendingIntent.getActivity(con,0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		mRemoteViews = new RemoteViews(con.getPackageName(), R.layout.music_player_notification);
		mRemoteViews.setTextViewText(R.id.songName, songs.get(0).get("songTitle"));
		Intent i1 = new Intent("com.shivsau.motiondetect.PAUSERESUME");
        PendingIntent pending1 = PendingIntent.getBroadcast(con.getApplicationContext(), 12, i1, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.playPause, pending1);
        
        Intent i2 = new Intent("com.shivsau.motiondetect.NEXTSONG");
        PendingIntent pending2 = PendingIntent.getBroadcast(con.getApplicationContext(), 13, i2, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.next, pending2);
        
        Intent i3 = new Intent("com.shivsau.motiondetect.PREVIOUSSONG");
        PendingIntent pending3 = PendingIntent.getBroadcast(con.getApplicationContext(), 14, i3, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.prev, pending3);
        
        Intent i4 = new Intent("com.shivsau.motiondetect.CLOSENOTIF");
        PendingIntent pending4 = PendingIntent.getBroadcast(con.getApplicationContext(), 15, i4, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.closeNotif, pending4);
        
        Intent i5 = new Intent("com.shivsau.motiondetect.SHUFFLESONG");
        PendingIntent pending5 = PendingIntent.getBroadcast(con.getApplicationContext(), 16, i5, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.shuffle, pending5);
        
        Intent i6 = new Intent("com.shivsau.motiondetect.REPEATSONG");
        PendingIntent pending6 = PendingIntent.getBroadcast(con.getApplicationContext(), 17, i6, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.repeat, pending6);
        
        mBuilder = new NotificationCompat.Builder(con);
        
        int apiVersion = Build.VERSION.SDK_INT;

        if (apiVersion < VERSION_CODES.HONEYCOMB) {
            mNotification = new Notification(R.drawable.ic_launcher,null, System.currentTimeMillis());
            mNotification.contentView = mRemoteViews;
            mNotification.setLatestEventInfo(con,null , null, pendingIntent);
            
            mNotification.flags |= Notification.FLAG_NO_CLEAR;
            mNotification.defaults |= Notification.DEFAULT_LIGHTS;

            mNotificationManager.notify(NOTIF_ID, mNotification);

        }else if (apiVersion >= VERSION_CODES.HONEYCOMB) {
            mBuilder.setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .setContent(mRemoteViews);

            mNotificationManager.notify(NOTIF_ID, mBuilder.build());
        }
        
	}
	
	public void updateNotification()
	{
		int api = Build.VERSION.SDK_INT;
		mRemoteViews.setTextViewText(R.id.songName, songs.get(currentSongIndex).get("songTitle"));
	    if (api < VERSION_CODES.HONEYCOMB) {
	        mNotificationManager.notify(NOTIF_ID, mNotification);
	    }else if (api >= VERSION_CODES.HONEYCOMB) {
	        mNotificationManager.notify(NOTIF_ID, mBuilder.build());
	    }
	}
	
}
