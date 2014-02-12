package com.versacomllc.audit.dao.impl;

import static com.versacomllc.audit.utils.Constants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.ScopeOfWorkDao;
import com.versacomllc.audit.data.ScopeOfWork;

public class ScopeOfWorkDaoImpl extends AbstractDaoImpl implements ScopeOfWorkDao {

	private SQLiteOpenHelper helper;

	public ScopeOfWorkDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}

	private ContentValues createContentValues(ScopeOfWork work){
		ContentValues values = new ContentValues();

		values.put(WORK_TYPE, work.getWorkType());
	
		values.put(DATE_OF_WORK,
				String.valueOf(getDateAsInt(work.getDateOfWork())));
		values.put(TECH_NAME, work.getTechName());
		values.put(TECH_ID, work.getTechId());
		values.put(AUDIT_ID, work.getAuditId());
		values.put(RID, work.getRid());
		return values;
	}
	
	@Override
	public void addSOW(ScopeOfWork scopeOfWork) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = createContentValues(scopeOfWork);
		// Inserting Row
		long id = db.insert(TABLE_NAME, null, values);
		scopeOfWork.setId(id);
		db.close();
	}
	
	public void updateSOW(ScopeOfWork scopeOfWork) {
		
		ContentValues values = createContentValues(scopeOfWork);
		// updating row
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.update(TABLE_NAME, values, ID + " = ?",	new String[] { String.valueOf(scopeOfWork.getId()) });
		Log.d(LOG_TAG, "Record updated: "+ rowEffected);
	
		db.close();
	}
	public void deleteSOW(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.delete(TABLE_NAME,  ID + " = ?",	new String[] { String.valueOf(id) });
		Log.d(LOG_TAG, "Record deleted: "+ rowEffected);
		
	}
	private List<ScopeOfWork> loadScopeOfWorks(Cursor cursor){
		List<ScopeOfWork> scopeofWorks = new ArrayList<ScopeOfWork>();
		
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				ScopeOfWork work = new ScopeOfWork();
				work.setId(cursor.getLong(index++));
				work.setWorkType(cursor.getString(index++));
				work.setDateOfWork(getLongAsDate(cursor.getLong(index++)));
				work.setTechName(cursor.getString(index++));
				work.setTechId(cursor.getString(index++));
				work.setAuditId(cursor.getLong(index++));
				work.setRid(cursor.getString(index++));
				scopeofWorks.add(work);

			} while (cursor.moveToNext());
		}

		return scopeofWorks;
	}
	@Override
	public List<ScopeOfWork> getScopeOfWorkByAuditId(final long auditId) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ AUDIT_ID +"=" + auditId;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<ScopeOfWork> scopeofWorks = loadScopeOfWorks(cursor);
		cursor.close();
		db.close();

		return scopeofWorks;

	}



	@Override
	public ScopeOfWork getScopeOfWorkById(long id) {
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ ID +"=" + id;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<ScopeOfWork> scopeofWorks = loadScopeOfWorks(cursor);
		cursor.close();
		db.close();
		if(scopeofWorks.size() >0){
			return scopeofWorks.get(0);
		}
		return null;
	}
}
