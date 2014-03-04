package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.LocalScopeOfWorkTech;

public interface ScopeOfWorkTechDao {
	public static final String TABLE_NAME = "scope_of_work_tech";

	public static final String ID = "id";
	public static final String RID = "rid";
	//public static final String AUDIT_ID = "a_id";
	public static final String SOW_ID = "sow_id";
	public static final String SYNC = "sync";

	public static final String TECH_NAME = "t_name";
	public static final String TECH_ID = "t_id";

	// Create table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + SOW_ID
			+ " INTEGER ," + TECH_NAME + " TEXT, " + TECH_ID + " TEXT ,"
			+ SYNC + " INTEGER ," + RID + " TEXT "
			+ ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	long addSOWTech(LocalScopeOfWorkTech scopeOfWorkTech);

	void updateSOWTech(LocalScopeOfWorkTech scopeOfWorkTech);

	void deleteSOWTech(long id);

	List<LocalScopeOfWorkTech> getScopeOfWorkTechBySOWId(final long sowId);
	
	public List<LocalScopeOfWorkTech> getPendingScopeOfWorkTechBySOWId(
			final long sowId);

	LocalScopeOfWorkTech getScopeOfWorkTechById(final long id);

	public int deleteSOWTechBySOWId(long sowId);

}
