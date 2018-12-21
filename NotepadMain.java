package com.shivsau.motiondetect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NotepadMain extends Fragment implements OnClickListener {
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
	Button del;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.notepad_main, container, false);
		body=(LineEditText) v.findViewById(R.id.body);
		del=(Button) v.findViewById(R.id.delAll);
		del.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String str=body.getText().toString();
		mh= new MyHelper3(getActivity());
		SQLiteDatabase db= mh.getWritableDatabase();
		db.execSQL("delete from notes");
		ContentValues cv = new ContentValues();
		cv.put("note",str);
		db.insert("notes", null, cv);
		db.close();
		cv.clear();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mh=new MyHelper3(getActivity());
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		body.setText("");
	}

}
