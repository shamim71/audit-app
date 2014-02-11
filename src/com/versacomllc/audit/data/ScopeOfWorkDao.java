package com.versacomllc.audit.data;

import java.util.List;

public interface ScopeOfWorkDao {
	public static final String TABLE_NAME = "scope_of_work";

	public static final String ID = "id";
	public static final String RID = "rid";
	public static final String AUDIT_ID = "aid";

	public static final String WORK_TYPE = "wtype";
	public static final String DATE_OF_WORK = "dofw";
	public static final String TECH_NAME = "t_name";
	public static final String TECH_ID = "t_id";

	// Create table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + WORK_TYPE
			+ " TEXT ," + DATE_OF_WORK + " INTEGER ," + TECH_NAME + " TEXT, "
			+ TECH_ID + " TEXT ," + AUDIT_ID + " INTEGER ," + RID + " TEXT "
			+ ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	void addSOW(ScopeOfWork scopeOfWork);
	
	void updateSOW(ScopeOfWork scopeOfWork);

	void deleteSOW(long id);
	
	List<ScopeOfWork> getScopeOfWorkByAuditId(final long auditId);
	
	ScopeOfWork getScopeOfWorkById(final long id);
}
