package com.versacomllc.audit.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.versacomllc.audit.R;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.data.LocalAuditDefect;
import com.versacomllc.audit.data.LocalScopeOfWorkTech;

public class SowTechListAdapter extends ArrayAdapter<LocalScopeOfWorkTech> {

	private Context mContext;
	private int resourceId;
	private List<LocalScopeOfWorkTech> items;

	public SowTechListAdapter(Context context, int resource,
			List<LocalScopeOfWorkTech> objects) {
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

			

			holder.tech = (TextView) convertView
					.findViewById(R.id.tv_tech_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LocalScopeOfWorkTech item = items.get(position);


		
		holder.tech.setText(item.getTechName());

		return convertView;
	}
	public List<LocalScopeOfWorkTech> getAllItems(){
		return this.items;
	}
	static class ViewHolder {

		TextView tech;

	}

}
