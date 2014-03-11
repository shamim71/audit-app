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

public class AuditListAdapter extends ArrayAdapter<LocalAudit> {

	private Context mContext;
	private int resourceId;
	private List<LocalAudit> audits;

	public AuditListAdapter(Context context, int resource,
			List<LocalAudit> objects) {
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

			holder.customer = (TextView) convertView
					.findViewById(R.id.tv_customer);

			holder.siteId = (TextView) convertView
					.findViewById(R.id.tv_site_id);

			holder.auditedBy = (TextView) convertView
					.findViewById(R.id.tv_auditedBy);

			holder.auditDate = (TextView) convertView
					.findViewById(R.id.tv_audit_date);

			holder.auditStatus = (TextView) convertView
					.findViewById(R.id.tv_audit_status);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LocalAudit item = audits.get(position);

		final String customerText = getContext().getString(
				R.string.label_customer)
				+" "+ item.getCustomerName();
		final String siteText = getContext().getString(R.string.label_site_id)
				+" "+ item.getSiteId();

		final String auditorText = getContext().getString(
				R.string.label_audited_by)
				+" "+ item.getAuditedBy();
		final String auditDateText = getContext().getString(
				R.string.label_audit_date)
				+" "+ item.getAuditDate();
		final String auditStatusText = getContext().getString(
				R.string.label_result)
				+" "+ item.getAuditResult();

		holder.customer.setText(customerText);
		holder.siteId.setText(siteText);

		holder.auditedBy.setText(auditorText);
		holder.auditDate.setText(auditDateText);
		holder.auditStatus.setText(auditStatusText);

		return convertView;
	}

	static class ViewHolder {
		TextView customer;
		TextView siteId;
		TextView auditStatus;
		TextView auditedBy;
		TextView auditDate;

	}

}
