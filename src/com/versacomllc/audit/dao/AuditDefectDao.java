package com.versacomllc.audit.dao;

import java.util.List;

import com.versacomllc.audit.data.LocalAuditDefect;

public interface AuditDefectDao {

	public static final String TABLE_NAME = "audit_defect";

	public static final String ID = "local_id";
	public static final String AUDIT_RID = "a_rid";
	public static final String DEFECT_RID = "d_rid";
	public static final String TECH_RID = "t_rid";
	public static final String TECH_NAME = "t_name";
	public static final String DEFECT_COUNT = "d_count";
	public static final String DEFECT_NOTE = "d_note";
	public static final String CODE = "d_code";
	public static final String DESCRIPTION = "d_description";
	public static final String SEVERITY = "severity";
	public static final String PIC_BEFORE = "pic_b";
	public static final String PIC_AFTER = "pic_a";
	public static final String RID = "rid";
	
	// Create audit table
	public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE "
			+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + AUDIT_RID
			+ " TEXT," + DEFECT_RID + " TEXT, " + TECH_RID + " TEXT, " +  TECH_NAME + " TEXT, " + DEFECT_COUNT
			+ " INTEGER," + DEFECT_NOTE + " TEXT, " + CODE + " TEXT, " + DESCRIPTION
			+ " TEXT, " + SEVERITY	+ " TEXT, " + PIC_BEFORE + " TEXT, " + PIC_AFTER + " TEXT, " + RID + " TEXT " + ")";

	public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;



	public List<LocalAuditDefect> getAuditDefectByAuditId(final String auditId);
	
	public long addAuditDefect(LocalAuditDefect auditDefect);
	
	public long updateAuditDefect(LocalAuditDefect auditDefect);
	
	public int deleteAuditDefect(long localId);
	
	public LocalAuditDefect getAuditDefectByLocalId(long localId);
}
