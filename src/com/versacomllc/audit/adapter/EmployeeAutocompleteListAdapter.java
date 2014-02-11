package com.versacomllc.audit.adapter;


import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.versacomllc.audit.data.Employee;

public class EmployeeAutocompleteListAdapter extends ArrayAdapter<Employee> {

	private Context mContext;
	private int resourceId;

	private final Object lock = new Object();
	
	private List<Employee> mOriginalValues;

    private List<Employee> mActivitiesList;
    private Filter mFilter;
    
	public EmployeeAutocompleteListAdapter(Context context, int resource, List<Employee> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.resourceId = resource;
		this.mActivitiesList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	      ViewHolder holder;
	      if (convertView == null) {
	    	  
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(resourceId, parent, false);

	        holder = new ViewHolder();

	        holder.name = (TextView ) convertView.findViewById(android.R.id.text1);

	        convertView.setTag(holder);
	      }
	      else {
	        holder = (ViewHolder) convertView.getTag();
	      }
	      
	      Employee item = mActivitiesList.get(position);
	      
	      holder.name.setText(item.getName());


	      
		return convertView;
	}

	
	@Override
	public int getCount() {
		return mActivitiesList != null ? mActivitiesList.size() : 0;
	}



	@Override
	public Employee getItem(int position) {
		
		return mActivitiesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return super.getItemId(position);
	}

	@Override
	public int getPosition(Employee item) {
		return mActivitiesList.indexOf(item);
	}

	public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }
	public class ArrayFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			 FilterResults results = new FilterResults();

             if (mOriginalValues  == null) {
                 synchronized (lock) {
                 	mOriginalValues  = new ArrayList<Employee>(mActivitiesList);
                 }
             }

             if (prefix == null || prefix.length() == 0) {
                 synchronized (lock) {
                     List<Employee> list = new ArrayList<Employee>(mOriginalValues);
                     results.values = list;
                     results.count = list.size();
                 }
             } else {
                 final String prefixString = prefix.toString().toLowerCase();

                 List<Employee> values = mOriginalValues;
                 int count = values.size();

                 List<Employee> newValues = new ArrayList<Employee>(count);

                 for (int i = 0; i < count; i++) {
                	 Employee item = values.get(i);

                     String[] words = item.getName().toString().toLowerCase().split(" ");
                     int wordCount = words.length;

                     for (int k = 0; k < wordCount; k++) {
                         final String word = words[k];

                         if (word.startsWith(prefixString)) {
                             newValues.add(item);
                             break;
                         }
                     }
                 }

                 results.values = newValues;
                 results.count = newValues.size();
             }

             return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
				
	
			 mActivitiesList = (List<Employee>) results.values;

             if (results.count > 0) {
                 notifyDataSetChanged();
             } else {
                 notifyDataSetInvalidated();
             }
			
		}
		
	}




	static class ViewHolder {
		  TextView  name;
	  }
	  
}
