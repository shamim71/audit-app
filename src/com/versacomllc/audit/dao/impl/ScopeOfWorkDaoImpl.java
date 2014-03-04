package com.versacomllc.audit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versacomllc.audit.dao.ScopeOfWorkDao;
import com.versacomllc.audit.data.LocalScopeOfWork;
import com.versacomllc.audit.utils.Constants;

public class ScopeOfWorkDaoImpl extends AbstractDaoImpl implements ScopeOfWorkDao {

	private SQLiteOpenHelper helper;

	public ScopeOfWorkDaoImpl(SQLiteOpenHelper helper) {
		super();
		this.helper = helper;
	}

	private ContentValues createContentValues(LocalScopeOfWork work){
		ContentValues values = new ContentValues();

		values.put(WORK_TYPE, work.getWorkType());
	
		values.put(DATE_OF_WORK,
				String.valueOf(getDateAsInt(work.getDateOfWork())));
		values.put(AUDIT_ID, work.getAuditId());
		values.put(SYNC, work.getSync());
		values.put(RID, work.getRid());
		return values;
	}
	
	@Override
	public long addSOW(LocalScopeOfWork scopeOfWork) {
		scopeOfWork.setRid("-1");
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = createContentValues(scopeOfWork);
		// Inserting Row
		long id = db.insert(TABLE_NAME, null, values);
		scopeOfWork.setId(id);
		db.close();
		return id;
	}
	
	public void updateSOW(LocalScopeOfWork scopeOfWork) {
		
		ContentValues values = createContentValues(scopeOfWork);
		// updating row
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.update(TABLE_NAME, values, ID + " = ?",	new String[] { String.valueOf(scopeOfWork.getId()) });
		//Log.d(LOG_TAG, "Record updated: "+ rowEffected);
	
		db.close();
	}
	public void deleteSOW(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.delete(TABLE_NAME,  ID + " = ?",	new String[] { String.valueOf(id) });
		//Log.d(LOG_TAG, "Record deleted: "+ rowEffected);
		
	}
	
	public int deleteSOWByAuditId(String auditId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowEffected =  db.delete(TABLE_NAME,  AUDIT_ID + " = ?",	new String[] { auditId });
		Log.d(Constants.LOG_TAG, "Record deleted by audit id "+ rowEffected);
		return rowEffected;
	}
	
	private List<LocalScopeOfWork> loadScopeOfWorks(Cursor cursor){
		List<LocalScopeOfWork> scopeofWorks = new ArrayList<LocalScopeOfWork>();
		
		if (cursor.moveToFirst()) {
			do {
				int index = 0;
				LocalScopeOfWork work = new LocalScopeOfWork();
				work.setId(cursor.getLong(index++));
				work.setWorkType(cursor.getString(index++));
				work.setDateOfWork(getLongAsDate(cursor.getLong(index++)));
				work.setAuditId(cursor.getLong(index++));
				work.setSync(cursor.getInt(index++));
				work.setRid(cursor.getString(index++));
				scopeofWorks.add(work);

			} while (cursor.moveToNext());
		}

		return scopeofWorks;
	}
	@Override
	public List<LocalScopeOfWork> getScopeOfWorkByAuditId(final long auditId) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ AUDIT_ID +"=" + auditId;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalScopeOfWork> scopeofWorks = loadScopeOfWorks(cursor);
		cursor.close();
		db.close();

		return scopeofWorks;

	}
	@Override
	public List<LocalScopeOfWork> getPendingScopeOfWorkByAuditId(final long auditId) {

		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ AUDIT_ID +"=? and "+ SYNC + "=?";
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(auditId), "0" });
		List<LocalScopeOfWork> scopeofWorks = loadScopeOfWorks(cursor);
		cursor.close();
		db.close();

		return scopeofWorks;

	}


	@Override
	public LocalScopeOfWork getScopeOfWorkById(long id) {
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where "+ ID +"=" + id;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		List<LocalScopeOfWork> scopeofWorks = loadScopeOfWorks(cursor);
		cursor.close();
		db.close();
		if(scopeofWorks.size() >0){
			return scopeofWorks.get(0);
		}
		return null;
	}
}
