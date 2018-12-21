package com.shivsau.motiondetect;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Model> {

	  private final List<Model> list;
	  int pos;
	  private final Activity context;
	  

	  public MyAdapter(Activity context, List<Model> list) {
	    super(context, R.layout.playlist_item, list);
	    this.context = context;
	    this.list = list;
	  }

	  static class ViewHolder {
	    protected TextView text;
	    protected CheckBox checkbox;
	  }

	  @SuppressLint("InflateParams")
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View view = null;
	    pos=position;
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.playlist_item, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) view.findViewById(R.id.songTitle);
	      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checking);
	      viewHolder.checkbox
	          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(CompoundButton buttonView,
	                boolean isChecked) {
	              Model element = (Model) viewHolder.checkbox
	                  .getTag();
	              element.setSelected(buttonView.isChecked());
	            }
	          });
	      view.setTag(viewHolder);
	      viewHolder.checkbox.setTag(list.get(position));
	    } else {
	      view = convertView;
	      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
	    }
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.text.setText(list.get(position).getName());
	    holder.checkbox.setChecked(list.get(position).isSelected());
	    return view;
	  }
	} 
