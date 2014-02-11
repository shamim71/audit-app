package com.versacomllc.audit.data.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.versacomllc.audit.data.SiteWorkTypeDao;

public class SiteWorkTypeDaoImpl implements SiteWorkTypeDao {

	private SQLiteOpenHelper helper;

	public SiteWorkTypeDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}

	@Override
	public void addWorkTypes(List<String> workTypes) {
		SQLiteDatabase db = helper.getWritableDatabase();

		for (String type : workTypes) {
			
			if(!isWorkTypeExist(db,type)){
				ContentValues values = new ContentValues();
				values.put(NAME, type);
				db.insert(TABLE_NAME, null, values);
			}

		}
		db.close();

	}
	public boolean isWorkTypeExist(SQLiteDatabase db, String type) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where " + NAME
				+ "='" + type + "'";
		boolean found = false;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			found = true;
		}
		cursor.close();

		return found;
	}
	@Override
	public List<String> getWorkTypes() {
		List<String> types = new ArrayList<String>();

		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				int index = 1;
				String name = cursor.getString(index);
				types.add(name);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return types;
	}

}
