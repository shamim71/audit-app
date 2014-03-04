package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.LocalScopeOfWork;

public interface ScopeOfWorkDao {
	public static final String TABLE_NAME = "scope_of_work";

	public static final String ID = "id";
	public static final String RID = "rid";
	public static final String AUDIT_ID = "aid";
	public static final String SYNC = "sync";
	
	public static final String WORK_TYPE = "wtype";
	public static final String DATE_OF_WORK = "dofw";


	// Create table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + WORK_TYPE
			+ " TEXT ," + DATE_OF_WORK + " INTEGER ," + AUDIT_ID + " INTEGER ," + SYNC + " INTEGER ,"+ RID + " TEXT "
			+ ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	long addSOW(LocalScopeOfWork scopeOfWork);
	
	void updateSOW(LocalScopeOfWork scopeOfWork);

	void deleteSOW(long id);
	
	List<LocalScopeOfWork> getScopeOfWorkByAuditId(final long auditId);
	
	public List<LocalScopeOfWork> getPendingScopeOfWorkByAuditId(final long auditId);
	
	LocalScopeOfWork getScopeOfWorkById(final long id);
	
	public int deleteSOWByAuditId(String auditId);
	
}
