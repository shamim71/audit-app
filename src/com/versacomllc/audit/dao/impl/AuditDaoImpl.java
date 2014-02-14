package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.AuditDao;
import com.versacomllc.audit.data.LocalAudit;
import com.versacomllc.audit.utils.Constants;

public class AuditDaoImpl implements AuditDao {

	private SQLiteOpenHelper helper;

	public AuditDaoImpl(SQLiteOpenHelper helper) {
		this.helper = helper;

	}

	@Override
	public long addInternalAudit(LocalAudit audit) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = createContentValues(audit);
		// Inserting Row
		long id = db.insert(TABLE_INTERNAL_AUDITS, null, values);
		db.close();
		return id;
	}

	private ContentValues createContentValues(LocalAudit audit) {
		ContentValues values = new ContentValues();

		values.put(AUDIT_TYPE, audit.getAuditType());
		values.put(AUDIT_STATUS, audit.getAuditStatus());
		values.put(AUDIT_DATE,
				String.valueOf(getDateAsInt(audit.getAuditDate())));
		values.put(AUDIT_HOUR, audit.getAuditHour());
		values.put(AUDIT_BY, audit.getAuditedBy());
		values.put(AUDIT_BY_EMPLOYEE, audit.getAuditedByEmployee());
		values.put(AUDIT_SITE_ID, audit.getSiteId());
		values.put(AUDIT_CUSTOMER, audit.getCustomer());
		values.put(AUDIT_CUSTOMER_NAME, audit.getCustomerName());
		values.put(RID, audit.getRid());
		return values;
	}

	@Override
	public void updateInternalAudit(LocalAudit audit) {

		ContentValues values = createContentValues(audit);
		// updating row
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected = db.update(TABLE_INTERNAL_AUDITS, values, ID + " = ?",
				new String[] { String.valueOf(audit.getId()) });
		Log.d(LOG_TAG, "Record updated: " + rowEffected);
		db.close();
	}

	public void deleteInternalAudit(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected = db.delete(TABLE_INTERNAL_AUDITS, ID + " = ?",
				new String[] { String.valueOf(id) });
		Log.d(LOG_TAG, "Record deleted: " + rowEffected);
		db.close();
	}

	@Override
	public List<LocalAudit> getAllInternalAudits() {
		String selectQuery = "SELECT  * FROM " + TABLE_INTERNAL_AUDITS;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalAudit> auditList = loadAudits(cursor);
		cursor.close();
		db.close();

		return auditList;
	}

	@Override
	public LocalAudit getInternalAuditsById(String id) {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_INTERNAL_AUDITS
				+ " where " + ID + "=" + id + "";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalAudit> auditList = loadAudits(cursor);
		cursor.close();
		db.close();
		if (auditList.size() > 0) {
			return auditList.get(0);
		}
		return null;
	}

	@Override
	public List<LocalAudit> getAllInternalAuditsByEmployee(String userId) {
		String selectQuery = "SELECT  * FROM " + TABLE_INTERNAL_AUDITS
				+ " where " + AUDIT_BY_EMPLOYEE + "='" + userId + "'";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalAudit> auditList = loadAudits(cursor);
		cursor.close();
		db.close();
		return auditList;
	}

	private List<LocalAudit> loadAudits(Cursor cursor) {
		List<LocalAudit> auditList = new ArrayList<LocalAudit>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				LocalAudit audit = new LocalAudit();
				audit.setId(cursor.getLong(index++));
				audit.setAuditType(cursor.getString(index++));
				audit.setAuditStatus(cursor.getString(index++));

				long auditTime = Long.parseLong(cursor.getString(index++));
				audit.setAuditDate(getLongAsDate(auditTime));

				audit.setAuditHour(cursor.getString(index++));
				audit.setAuditedBy(cursor.getString(index++));
				audit.setAuditedByEmployee(cursor.getString(index++));
				audit.setSiteId(cursor.getString(index++));
				audit.setCustomer(cursor.getString(index++));
				audit.setCustomerName(cursor.getString(index++));

				audit.setRid(cursor.getString(index++));
				auditList.add(audit);

			} while (cursor.moveToNext());
		}

		return auditList;
	}

	private long getDateAsInt(String date) {
		try {
			Date dt = Constants.US_DATEFORMAT.parse(date);
			return dt.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	private String getLongAsDate(long val) {
		Date dt = new Date(val);
		return Constants.US_DATEFORMAT.format(dt);
	}
}
