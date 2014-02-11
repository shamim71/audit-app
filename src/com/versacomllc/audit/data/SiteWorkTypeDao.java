package com.versacomllc.audit.data;

import java.util.List;


public interface SiteWorkTypeDao {

	public static final String TABLE_NAME = "site_work_type";

	public static final String ID = "id";

	public static final String NAME = "name";

	// Create  table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT "
			+ ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	void addWorkTypes(List<String> workTypes);
	
	List<String> getWorkTypes();
}
