package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.AuditDefectDao;
import com.versacomllc.audit.data.LocalAuditDefect;

public class AuditDefectDaoImpl implements AuditDefectDao {

	private SQLiteOpenHelper helper;

	public AuditDefectDaoImpl(SQLiteOpenHelper helper) {
		this.helper = helper;
	}

	@Override
	public List<LocalAuditDefect> getAuditDefectByAuditId(String auditId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "
				+ AUDIT_RID + "='" + auditId + "'";

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalAuditDefect> list = loadAuditDefects(cursor);
		cursor.close();

		return list;
	}

	@Override
	public long addAuditDefect(LocalAuditDefect auditDefect) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = createContentValues(auditDefect);

		long id = db.insert(TABLE_NAME, null, values);
		db.close();
		return id;
	}

	private ContentValues createContentValues(LocalAuditDefect ad) {
		
		ContentValues values = new ContentValues();
		values.put(AUDIT_RID, ad.getAuditId());
		values.put(DEFECT_RID, ad.getDefectId());
		values.put(TECH_RID, ad.getTechId());
		values.put(TECH_NAME, ad.getTechName());
		values.put(DEFECT_COUNT, ad.getCount());
		values.put(DEFECT_NOTE, ad.getNote());
		values.put(CODE, ad.getDefectCode());
		values.put(DESCRIPTION, ad.getDefectDescription());
		values.put(SEVERITY, ad.getDefectSeverity());
		values.put(PIC_BEFORE, ad.getDefectPicBefore());
		values.put(PIC_AFTER, ad.getDefectPicAfter());
		values.put(RID, ad.getId());
		
		return values;
	}

	private List<LocalAuditDefect> loadAuditDefects(Cursor cursor) {
		List<LocalAuditDefect> list = new ArrayList<LocalAuditDefect>();
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				LocalAuditDefect obj = new LocalAuditDefect();
				obj.setLocalId(cursor.getLong(index++));
				obj.setAuditId(cursor.getString(index++));
				obj.setDefectId(cursor.getString(index++));
				obj.setTechId(cursor.getString(index++));
				obj.setTechName(cursor.getString(index++));
				obj.setCount(String.valueOf(cursor.getInt(index++)));
				obj.setNote(cursor.getString(index++));
				obj.setDefectCode(cursor.getString(index++));
				obj.setDefectDescription(cursor.getString(index++));
				obj.setDefectSeverity(cursor.getString(index++));
				obj.setDefectPicBefore(cursor.getString(index++));
				obj.setDefectPicAfter(cursor.getString(index++));
				obj.setId(cursor.getString(index++));
				list.add(obj);

			} while (cursor.moveToNext());
		}

		return list;
	}

	@Override
	public int deleteAuditDefect(long localId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected = db.delete(TABLE_NAME, ID + " = ?",
				new String[] { String.valueOf(localId) });
		Log.d(LOG_TAG, "Record deleted: " + rowEffected);
		db.close();
		return rowEffected;
	}

	@Override
	public LocalAuditDefect getAuditDefectByLocalId(long localId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "
				+ ID + "=" + localId + "";

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalAuditDefect> list = loadAuditDefects(cursor);
		cursor.close();
		
		if(list.size() > 0){
			return list.get(0);
		}
		
		return null;
	}

	@Override
	public long updateAuditDefect(LocalAuditDefect auditDefect) {
		ContentValues values = createContentValues(auditDefect);
		// updating row
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected = db.update(TABLE_NAME, values, ID + " = ?",
				new String[] { String.valueOf(auditDefect.getLocalId()) });
		Log.d(LOG_TAG, "Record updated: " + rowEffected);
		db.close();
		
		return rowEffected;
	}

}
