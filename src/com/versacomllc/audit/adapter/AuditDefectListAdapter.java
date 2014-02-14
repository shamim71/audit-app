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

public class AuditDefectListAdapter extends ArrayAdapter<LocalAuditDefect> {

	private Context mContext;
	private int resourceId;
	private List<LocalAuditDefect> audits;

	public AuditDefectListAdapter(Context context, int resource,
			List<LocalAuditDefect> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.resourceId = resource;
		this.audits = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {

			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(resourceId, parent, false);

			holder = new ViewHolder();

			holder.severity = (TextView) convertView
					.findViewById(R.id.tv_defect_severity);

			holder.count = (TextView) convertView
					.findViewById(R.id.tv_defect_count);

			holder.tech = (TextView) convertView
					.findViewById(R.id.tv_tech_name);

			holder.code = (TextView) convertView
					.findViewById(R.id.tv_defect_code);

			holder.description = (TextView) convertView
					.findViewById(R.id.tv_defect_description);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LocalAuditDefect item = audits.get(position);

		holder.severity.setText("Severity: "+item.getDefectSeverity());
		holder.count.setText("Found "+ item.getCount() + " time(s)");
		
		holder.description.setText(item.getDefectDescription());
		holder.code.setText("Code: "+item.getDefectCode());
		
		holder.tech.setText("Responsible Tech: "+item.getTechName());

		return convertView;
	}

	static class ViewHolder {
		TextView severity;
		TextView code;
		TextView description;
		
		TextView count;
		TextView notes;
		TextView tech;

	}

}
