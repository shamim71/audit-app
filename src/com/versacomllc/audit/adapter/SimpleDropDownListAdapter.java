package com.versacomllc.audit.adapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.versacomllc.audit.model.Customer;

public class SimpleDropDownListAdapter extends ArrayAdapter<Customer> {

	private Context mContext;
	private int resourceId;
	private List<Customer> items;

	public SimpleDropDownListAdapter(Context context, int resource, List<Customer> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.resourceId = resource;
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	      ViewHolder holder;
	      if (convertView == null) {
	    	  
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(resourceId, parent, false);

	        holder = new ViewHolder();

	        holder.name = (TextView) convertView.findViewById(android.R.id.text1);

	        convertView.setTag(holder);
	      }
	      else {
	        holder = (ViewHolder) convertView.getTag();
	      }
	      
	      Customer item = items.get(position);
	      
	      holder.name.setText(item.getName());


	      
		return convertView;
	}

	

	  static class ViewHolder {
	    TextView name;
	  }
	  
}
