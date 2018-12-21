package com.shivsau.motiondetect;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.EditText;

public class NotepadLock extends Activity {
	public static class LineEditText extends EditText{
		  public LineEditText(Context context, AttributeSet attrs) {
		   super(context, attrs);
		    mRect = new Rect();
		          mPaint = new Paint();
		          mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		          mPaint.setARGB(100, 255, 99, 0);
		  }

		  private Rect mRect;
		     private Paint mPaint;     
		     
		     @Override
		     protected void onDraw(Canvas canvas) {
		   
		         int height = getHeight();
		         int line_height = getLineHeight();

		         int count = height / line_height;

		         if (getLineCount() > count)
		             count = getLineCount();

		         Rect r = mRect;
		         Paint paint = mPaint;
		         int baseline = getLineBounds(0, r);

		         for (int i = 0; i < count; i++) {

		             canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
		             baseline += getLineHeight();

		         super.onDraw(canvas);
		     }

		 }
	}

	MyHelper3 mh;
	LineEditText body;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
		WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
		|WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.notepad_above_lock);
		ActionBar ab = getActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9900")));
		body=(LineEditText) findViewById(R.id.body);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String str=body.getText().toString();
		mh= new MyHelper3(this);
		SQLiteDatabase db= mh.getWritableDatabase();
		db.execSQL("delete from notes");
		ContentValues cv = new ContentValues();
		cv.put("note",str);
		db.insert("notes", null, cv);
		db.close();
		cv.clear();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mh=new MyHelper3(this);
		SQLiteDatabase db = mh.getReadableDatabase();
		Cursor c= db.rawQuery("SELECT * FROM notes",null);
		while(c.moveToNext())
		{
			body.setText(c.getString(0));
		}
		c.close();
		db.close();
		body.setSelection(body.length());
	}
	
}
