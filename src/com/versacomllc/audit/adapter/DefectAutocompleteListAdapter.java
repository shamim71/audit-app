package com.versacomllc.audit.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.versacomllc.audit.data.LocalDefect;

public class DefectAutocompleteListAdapter extends ArrayAdapter<LocalDefect> {

	private Context mContext;
	private int resourceId;

	private final Object lock = new Object();

	private List<LocalDefect> mOriginalValues;

	private List<LocalDefect> mActivitiesList;

	private Filter mFilter;

	public DefectAutocompleteListAdapter(Context context, int resource,
			List<LocalDefect> objects) {
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

			holder.name = (TextView) convertView
					.findViewById(android.R.id.text1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LocalDefect item = mActivitiesList.get(position);
		final String text = "" + item.getCode() + " > " + item.getCategory()
				+ " > " + item.getDescription();
		holder.name.setText(text);

		return convertView;
	}

	@Override
	public int getCount() {
		return mActivitiesList != null ? mActivitiesList.size() : 0;
	}

	@Override
	public LocalDefect getItem(int position) {

		return mActivitiesList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return super.getItemId(position);
	}

	@Override
	public int getPosition(LocalDefect item) {
		return mActivitiesList.indexOf(item);
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	public class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (lock) {
					mOriginalValues = new ArrayList<LocalDefect>(
							mActivitiesList);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (lock) {
					List<LocalDefect> list = new ArrayList<LocalDefect>(
							mOriginalValues);
					results.values = list;
					results.count = list.size();
				}
			} else {
				final String prefixString = prefix.toString().toLowerCase();

				List<LocalDefect> values = mOriginalValues;
				int count = values.size();

				List<LocalDefect> newValues = new ArrayList<LocalDefect>(count);

				for (int i = 0; i < count; i++) {
					LocalDefect item = values.get(i);

					if(item.getCode().toLowerCase().contains(prefixString)){
						newValues.add(item);
						continue;
					}
					if(item.getCategory().toLowerCase().contains(prefixString)){
						newValues.add(item);
						continue;
					}
					if(item.getDescription().toLowerCase().contains(prefixString)){
						newValues.add(item);
						continue;
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

			mActivitiesList = (List<LocalDefect>) results.values;

			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

	}

	static class ViewHolder {
		TextView name;
	}

}
