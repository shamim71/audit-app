package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.LocalDefect;
import com.versacomllc.audit.model.Defect;

public interface DefectDao {
	public static final String TABLE_NAME = "defect";

	public static final String ID = "local_id";
	public static final String RID = "rid";

	// INTERNAL AUDIT Table Columns names
	public static final String CODE = "code";
	public static final String CATEGORY = "category";
	public static final String SUB_CATEGORY = "sub_category";
	public static final String DESCRIPTION = "description";
	public static final String SEVERITY = "severity";

	// Create audit table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + CODE
			+ " TEXT," + CATEGORY + " TEXT, " + SUB_CATEGORY + " TEXT, " + DESCRIPTION
			+ " TEXT, " + SEVERITY	+ " TEXT, " + RID + " TEXT " + ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;



	List<LocalDefect> getAllDefects();
	
	void addDefectList(List<Defect> defects);


}
