package com.versacomllc.audit.adapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.versacomllc.audit.model.CheckedInventoryItem;
import com.versacomllc.audit.R;

public class CheckoutListAdapter extends ArrayAdapter<CheckedInventoryItem> {

	private Context mContext;
	private int resourceId;
	private List<CheckedInventoryItem> items;

	public CheckoutListAdapter(Context context, int resource, List<CheckedInventoryItem> objects) {
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

	        holder.name = (TextView) convertView.findViewById(R.id.tv_code);

	        holder.description = (TextView) convertView
	            .findViewById(R.id.tv_description);
	        holder.quantity = (TextView) convertView.findViewById(R.id.tv_quantity);


	        convertView.setTag(holder);
	      }
	      else {
	        holder = (ViewHolder) convertView.getTag();
	      }
	      
	      CheckedInventoryItem item = items.get(position);
	      
	      holder.name.setText(item.getName());
	      holder.description.setText(item.getFullName());
	
	      holder.quantity.setText(String.valueOf(item.getCount()));
	      
		return convertView;
	}

	

	  static class ViewHolder {
	    TextView name;
	    TextView description;
	    TextView quantity;

	  }
	  
}
