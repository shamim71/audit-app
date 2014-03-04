package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.ScopeOfWorkTechDao;
import com.versacomllc.audit.data.LocalScopeOfWork;
import com.versacomllc.audit.data.LocalScopeOfWorkTech;
import com.versacomllc.audit.utils.Constants;

public class ScopeOfWorkTechDaoImpl extends AbstractDaoImpl implements ScopeOfWorkTechDao {

	private SQLiteOpenHelper helper;

	public ScopeOfWorkTechDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}

	private ContentValues createContentValues(LocalScopeOfWorkTech work){
		ContentValues values = new ContentValues();

		values.put(TECH_NAME, work.getTechName());
		values.put(TECH_ID, work.getTechId());
		values.put(SOW_ID, work.getSowId());
		values.put(SYNC, work.getSync());
		values.put(RID, work.getRid());
		return values;
	}
	
	@Override
	public long addSOWTech(LocalScopeOfWorkTech scopeOfWorkTech) {

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = createContentValues(scopeOfWorkTech);
		// Inserting Row
		long id = db.insert(TABLE_NAME, null, values);
		scopeOfWorkTech.setId(id);
		db.close();
		return id;
	}

	@Override
	public void updateSOWTech(LocalScopeOfWorkTech scopeOfWorkTech) {
		ContentValues values = createContentValues(scopeOfWorkTech);
		// updating row
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.update(TABLE_NAME, values, ID + " = ?",	new String[] { String.valueOf(scopeOfWorkTech.getId()) });
		Log.d(LOG_TAG, "Record updated: "+ rowEffected);
	
		db.close();
		
	}

	@Override
	public void deleteSOWTech(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.delete(TABLE_NAME,  ID + " = ?",	new String[] { String.valueOf(id) });
		Log.d(LOG_TAG, "Record deleted: "+ rowEffected);
		
	}



	@Override
	public List<LocalScopeOfWorkTech> getScopeOfWorkTechBySOWId(long sowId) {
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ SOW_ID +"=" + sowId;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalScopeOfWorkTech> scopeofWorks = loadScopeOfWorkTechs(cursor);
		cursor.close();
		db.close();

		return scopeofWorks;

	}

	private List<LocalScopeOfWorkTech> loadScopeOfWorkTechs(Cursor cursor) {
		List<LocalScopeOfWorkTech> scopeofWorkTechs = new ArrayList<LocalScopeOfWorkTech>();
		
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				LocalScopeOfWorkTech work = new LocalScopeOfWorkTech();
				work.setId(cursor.getLong(index++));
				work.setSowId(cursor.getLong(index++));
				work.setTechName(cursor.getString(index++));
				work.setTechId(cursor.getString(index++));
				work.setSync(cursor.getInt(index++));
				work.setRid(cursor.getString(index++));
				scopeofWorkTechs.add(work);

			} while (cursor.moveToNext());
		}

		return scopeofWorkTechs;
	}

	@Override
	public List<LocalScopeOfWorkTech> getPendingScopeOfWorkTechBySOWId(
			long sowId) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ SOW_ID +"=? and "+ SYNC + "=?";
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(sowId), "0" });
		List<LocalScopeOfWorkTech> scopeofWorks = loadScopeOfWorkTechs(cursor);
		cursor.close();
		db.close();

		return scopeofWorks;
	}

	@Override
	public LocalScopeOfWorkTech getScopeOfWorkTechById(long id) {

		return null;
	}

	@Override
	public int deleteSOWTechBySOWId(long sowId) {

		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.delete(TABLE_NAME,  SOW_ID + " = ?",	new String[] { String.valueOf(sowId) });
		Log.d(Constants.LOG_TAG, "Record deleted by SOW id "+ rowEffected);
		return rowEffected;
	}

	
}
