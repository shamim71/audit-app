package com.versacomllc.audit.adapter;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.versacomllc.audit.R;
import com.versacomllc.audit.data.DatabaseHandler;
import com.versacomllc.audit.data.LocalScopeOfWork;
import com.versacomllc.audit.data.LocalScopeOfWorkTech;
import com.versacomllc.audit.utils.Constants;

public class ScopeOfWorkListAdapter extends ArrayAdapter<LocalScopeOfWork> {

	private Context mContext;
	private int resourceId;
	private List<LocalScopeOfWork> works;
	DatabaseHandler dbHandler;
	public ScopeOfWorkListAdapter(Context context, int resource,
			List<LocalScopeOfWork> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.resourceId = resource;
		this.works = objects;
		dbHandler = new DatabaseHandler(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {

			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(resourceId, parent, false);

			holder = new ViewHolder();

			holder.workType = (TextView) convertView
					.findViewById(R.id.tv_scope_of_work);

			holder.workDate = (TextView) convertView
					.findViewById(R.id.tv_work_date);

			holder.techName = (TextView) convertView
					.findViewById(R.id.tv_work_done_by);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LocalScopeOfWork item = works.get(position);

		holder.workType.setText("Work Type: " + item.getWorkType());
		List<LocalScopeOfWorkTech> techs = dbHandler.getScopeOfWorkTechDao().getScopeOfWorkTechBySOWId(item.getId());
	
		
		String tNames = null;

		if (techs != null) {
			for (LocalScopeOfWorkTech t : techs) {
				if(tNames == null){
					tNames = t.getTechName();
				}else{
					tNames = tNames + ", " + t.getTechName();
				}
				
			}
		}
		if(tNames != null){
			holder.techName.setText("Tech Names: " + tNames);
		}
		
		holder.workDate.setText("Work Date: " + item.getDateOfWork());

		return convertView;
	}

	static class ViewHolder {

		TextView workType;
		TextView techName;
		TextView workDate;

	}

}
