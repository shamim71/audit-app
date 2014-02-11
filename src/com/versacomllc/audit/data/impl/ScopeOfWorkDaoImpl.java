package com.versacomllc.audit.data.impl;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.versacomllc.audit.data.ScopeOfWork;
import com.versacomllc.audit.data.ScopeOfWorkDao;

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
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

}
