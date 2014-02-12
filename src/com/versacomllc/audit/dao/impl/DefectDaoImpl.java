package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.DefectDao;
import com.versacomllc.audit.data.LocalDefect;
import com.versacomllc.audit.model.Defect;

public class DefectDaoImpl  extends AbstractDaoImpl implements DefectDao{

	private SQLiteOpenHelper helper;

	public DefectDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}


	@Override
	public List<LocalDefect> getAllDefects() {
	
		String selectQuery = "SELECT  * FROM " + TABLE_NAME ;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalDefect> list = loadAllDefects(cursor);
		cursor.close();
		
		return list;
	}
	public LocalDefect getDefectByRid(SQLiteDatabase db, String rid) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where " + RID
				+ "='" + rid + "'";

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalDefect> list = loadAllDefects(cursor);
		cursor.close();

		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public void addDefectList(List<Defect> defects) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (Defect d : defects) {
			
			LocalDefect existing = getDefectByRid(db, d.getId());
		
			ContentValues values = createContentValues(d);
			if (existing == null) {
				long id = db.insert(TABLE_NAME, null, values);
				Log.d(LOG_TAG, "Defect record added: " + id);
			} else {
				int rowEffected = db.update(TABLE_NAME, values, RID + " = ?",
						new String[] { String.valueOf(d.getId()) });
				Log.d(LOG_TAG, "Record updated: " + rowEffected);
			}
		}

		db.close();
	}
	private ContentValues createContentValues(Defect defect) {
		
		ContentValues values = new ContentValues();
		values.put(CODE, defect.getCode());
		values.put(CATEGORY, defect.getCategory());
		values.put(SUB_CATEGORY, defect.getSubCategory());
		values.put(DESCRIPTION, defect.getDescription());
		values.put(SEVERITY, defect.getSeverity());
		values.put(RID, defect.getId());
		
		return values;
	}
	private List<LocalDefect> loadAllDefects(Cursor cursor) {
		List<LocalDefect> list = new ArrayList<LocalDefect>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				LocalDefect obj = new LocalDefect();
				obj.setLocalId(cursor.getLong(index++));
				obj.setCode(cursor.getString(index++));
				obj.setCategory(cursor.getString(index++));
				obj.setSubCategory(cursor.getString(index++));
				obj.setDescription(cursor.getString(index++));
				obj.setSeverity(cursor.getString(index++));
				obj.setId(cursor.getString(index++));
				list.add(obj);

			} while (cursor.moveToNext());
		}

		return list;
	}
}
